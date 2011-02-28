package com.mc.redis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;

import com.mc.redis.exception.UnexpectedBytesException;
import com.mc.redis.exception.UnexpectedEndOfStreamException;

public class RedisUniversalProtocol implements RedisProtocol {

    @Override
    public void writeRequest(OutputStream out, String cmd, byte[]... args)
            throws RedisException {
        try {
            if (cmd == null) throw new NullPointerException("Command cannot be null in a request to Redis");
            out.write(MULTI_BULK_INDICATOR);
            int numParts = (args == null) ? 1 : (args.length + 1);
            out.write(intToByteArray(numParts));
            out.write(REDIS_DELIM);
            out.write(BULK_INDICATOR);
            out.write(intToByteArray(cmd.length()));
            out.write(REDIS_DELIM);
            out.write(cmd.getBytes(utf8));
            out.write(REDIS_DELIM);
            if (args != null) {
                for (int i=0; i<args.length; i++) {
                    byte[] bytes = args[i];
                    out.write(BULK_INDICATOR);
                    out.write(intToByteArray(bytes == null ? -1 : bytes.length));
                    out.write(REDIS_DELIM);
                    if (bytes != null) {
                        out.write(args[i]);
                        out.write(REDIS_DELIM);
                    }
                }
            }
            out.flush();
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }
    
    @Override
    public void writeRequest(OutputStream out, String cmd, String... args)
            throws RedisException {
        if (args == null) {
            writeRequest(out, cmd, (byte[][])null);
        } else {
            byte[][] byteArgs = new byte[args.length][];
            for (int i=0; i<args.length; i++) {
                byteArgs[i] = args[i] == null ? null : args[i].getBytes(utf8);
            }
            writeRequest(out, cmd, byteArgs);
        }
    }
    
    @Override
    public void writeRequest(OutputStream out, String cmd)
            throws RedisException {
        writeRequest(out, cmd, (byte[][])null);
    }

    @Override
    public String readSingleLineReply(InputStream in) throws RedisException {
        PushbackInputStream pin = new PushbackInputStream(in, 3);
        try {
            checkForErrorResponse(pin);
            ensureExpectedType(pin,  ResponseType.SINGLE_LINE);
            return readToEOL(pin);
        } catch (RedisException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }
    
    @Override
    public BigInteger readBigInt(InputStream in) throws RedisException {
        PushbackInputStream pin = new PushbackInputStream(in, 3);
        try {
            checkForErrorResponse(pin);
            ensureExpectedType(pin, ResponseType.NUMBER);
            String val = readToEOL(pin);
            return new BigInteger(val);
        } catch (RedisException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }
    
    @Override
    public int readSmallInt(InputStream in) throws RedisException {
        return readBigInt(in).intValue();
    }
    
    @Override
    public long readLong(InputStream in) throws RedisException {
        return readBigInt(in).longValue();
    }
    
    @Override
    public boolean readBoolean(InputStream in) throws RedisException {
        int val = readSmallInt(in);
        switch (val) {
        case 0:
            return false;
        case 1:
            return true;
        default:
            throw new RedisException("Unexpected value when checking a boolean: " + val);
        }
    }

    @Override
    public byte[] readBulkReplyBinary(InputStream in) throws RedisException {
        PushbackInputStream pin = new PushbackInputStream(in, 3);
        try {
            checkForErrorResponse(pin);
            if (isNullResponse(pin)) return null;
            ensureExpectedType(pin,  ResponseType.BULK);
            return readValue(pin);
        } catch (RedisException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }

    @Override
    public String readBulkReply(InputStream in, Charset charset) throws RedisException {
        byte[] bytes = readBulkReplyBinary(in);
        if (bytes == null) return null;
        return new String(bytes, charset);
    }

    @Override
    public String readBulkReply(InputStream in) throws RedisException {
        return readBulkReply(in, utf8);
    }
    
    @Override
    public byte[][] readMultiBulkReplyBinary(InputStream in) throws RedisException {
        PushbackInputStream pin = new PushbackInputStream(in, 3);
        try {
            checkForErrorResponse(pin);
            if (isNullResponse(pin)) return null;
            ensureExpectedType(pin, ResponseType.MULTI_BULK);
            
            int numBulks = readNumber(pin);
            byte[][] byteArrays = new byte[numBulks][];
            
            for (int i=0; i<numBulks; i++) {
                byteArrays[i] = readBulkReplyBinary(pin);
            }
            
            return byteArrays;
        } catch (RedisException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }
    
    @Override
    public String[] readMultiBulkReply(InputStream in, Charset charset) throws RedisException {
        byte[][] bytes = readMultiBulkReplyBinary(in);
        if (bytes == null) return null;
        String[] resp = new String[bytes.length];
        for (int i=0; i<bytes.length; i++) {
            if (bytes[i] == null) resp[i] = null;
            else resp[i] = new String(bytes[i], charset);
        }
        return resp;
    }
    
    @Override
    public String[] readMultiBulkReply(InputStream in) throws RedisException {
        return readMultiBulkReply(in, utf8);
    }

    /*
     * Convert the integer to a byte array containing the UFT-8 encoded equivalent
     */
    private byte[] intToByteArray(int number) {
        return String.valueOf(number).getBytes(utf8);
    }
    
    /*
     * At the beginning of a response, check for a '-' and throw an
     * exception with the message, if found
     */
    private void checkForErrorResponse(PushbackInputStream pin) throws IOException {
        int indicator = pin.read();
        switch (indicator) {
        case EOS:
            throw new UnexpectedEndOfStreamException();
        case MINUS:
            throw new RedisErrorResponse(readToEOL(pin));
        default:
            pin.unread(indicator);
        }
    }
    
    /*
     * For single-line & error messages, read all the bytes up to the /r/n
     * and return.
     * This method also removes the trailing /r/n.
     * 
     * NOTE:
     * This method is NOT safe for reading Bulk or Multi-Bulk messages, only
     * single-line, error, and potentially number responses
     */
    private String readToEOL(PushbackInputStream pin) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int b = pin.read();
            while (EOS != b && CR != b) {
                sb.append((char)b);
                b = pin.read();
            }
            if (EOS == b)
                throw new UnexpectedEndOfStreamException();
            if (CR == b) {
                int b2 = pin.read();
                if (LF == b2) 
                    return sb.toString();
                sb.append((char)b).append((char)b2);
            }
        }
    }

    /*
     * The thing about this method, is that it assumes you've already read
     * past the type indicator byte.  It reads the length of the value, and
     * then returns that many bytes.
     */
    private byte[] readValue(PushbackInputStream pin) throws IOException {
        int len = readNumber(pin);
        byte[] bytes = readBytes(pin, len);
        if (pin.read() == CR && pin.read() == LF) 
            return bytes;
        throw new UnexpectedBytesException("The Redis Response didn't end when expected");
    }
    
    /*
     * Returns the next 'n' bytes from the InputStream
     */
    private byte[] readBytes(PushbackInputStream pin, int len) throws IOException {
        byte[] bytes = new byte[len];
        int bytesRead = 0;
        while (bytesRead < len) {
            int newBytesRead = pin.read(bytes, bytesRead, len-bytesRead);
            if (EOS == newBytesRead) throw new UnexpectedEndOfStreamException();
            bytesRead += newBytesRead;
        }
        return bytes;
    }
    
    /*
     * Ensures the InputStream is at the beginning of a response of
     * the expected type.  Throws an exception if not.
     */
    private void ensureExpectedType(PushbackInputStream pin, ResponseType expected) throws IOException {
        char typeIndicator = (char) pin.read();
        if (expected.indicator != typeIndicator) {
            String msg = String.format(unexpectedFormat, expected, expected.indicator, typeIndicator);
            throw new RedisException(msg);
        }
    }
    
    /*
     * Bulk messages use a message length of -1 to indicate null
     */
    private boolean isNullResponse(PushbackInputStream pin) throws IOException {
        int type = pin.read();
        byte[] bytes = readBytes(pin, 2);
        if (bytes[0] == MINUS && bytes[1] == ONE) {
            if (CR == pin.read() && LF == pin.read())
                return true;
            throw new IllegalStateException("No CRLF terminator after nil bulk reply");
        }
        pin.unread(bytes);
        pin.unread(type);
        return false;
    }
    
    /*
     * Read the number starting at the current position and ending
     * at /r/n
     */
    private final int readNumber(PushbackInputStream in) throws IOException {
        int raw = in.read();
        int val = 0;
        while (CR != raw && EOS != raw) {
            Character.digit(raw, 10);
            val = val*10 + Character.digit(raw, 10);
            raw = in.read();
        }
        if (EOS != raw && LF == in.read()) {
            return val;
        }
        throw new IllegalStateException("Couldn't read segment length");
    }
    
    private static final int CR = 13;
    private static final int LF = 10;
    private static final int EOS = -1;
    private static final int MINUS = '-';
    private static final int ONE = '1';
    
    private static final Charset utf8 = Charset.forName("UTF-8");

    private static byte MULTI_BULK_INDICATOR = "*".getBytes(utf8)[0];
    private static byte BULK_INDICATOR = "$".getBytes(utf8)[0];
    private static byte[] REDIS_DELIM = "\r\n".getBytes(utf8);
    
    private static final String unexpectedFormat =
        "Expected a %s which begins with a '%s', got '%s' instead.";
}
