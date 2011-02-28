package com.mc.redis;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * Definition of methods for writing and reading the Redis Unified Protocol
 * to and from InputStreams and OutputStreams.
 * 
 * @author mcase
 *
 */
public interface RedisProtocol {
    
    public void writeRequest(OutputStream out, String cmd) throws RedisException;
    
    public void writeRequest(OutputStream out, String cmd, byte[]... args)
        throws RedisException;
    
    public void writeRequest(OutputStream out, String cmd, String... args)
        throws RedisException;
    
    /**
     * Read a single line reply from Redis and return the contents, without
     * the leading + or the trailing /r/n
     * @param in
     * @return
     * @throws RedisException
     */
    public String readSingleLineReply(InputStream in) throws RedisException;
    
    /**
     * Read an integer reply from Redis and return the value as a
     * Java int primitive.
     * <p>
     * Note that this method is subject to inaccuracies if the value
     * returned from Redis is larger than Integer.MAX_VALUE.
     * 
     * @param in
     * @return
     * @throws RedisException
     */
    public int readSmallInt(InputStream in) throws RedisException;
    
    /**
     * Read an integer reply from Redis and return the value as a
     * java.math.BigInteger.
     * @param in
     * @return
     * @throws RedisException
     */
    public BigInteger readBigInt(InputStream in) throws RedisException;
    
    /**
     * 
     * Read an integer reply from Redis and return the value as a
     * Java long primitive.
     * <p>
     * Note that this method is subject to inaccuracies if the value
     * returned from Redis is larger than Long.MAX_VALUE  (Though
     * I don't imagine there is any scenario where this would happen.)
     * @param in
     * @return
     * @throws RedisException
     */
    public long readLong(InputStream in) throws RedisException;
    
    /**
     * 
     * @param in
     * @return
     * @throws RedisException
     */
    public boolean readBoolean(InputStream in) throws RedisException;
    
    /**
     * Gets a Bulk Response from the provided stream and returns
     * it in raw form as a byte array.
     * <p>
     * This method blocks until the entire response is received.
     * 
     * @param in
     * @return
     * @throws RedisException 
     */
    public byte[] readBulkReplyBinary(InputStream in) throws RedisException;
    
    /**
     * Reads a Bulk Response from the provided stream and decodes
     * the response to a String using the UTF-8 Charset.
     * <p>
     * This method blocks until the entire response is received.
     * <p>
     * This is equivalent to:
     * <pre>new String(getBulkResponse(in), Charset.forName("UTF-8"));</pre>
     * 
     * @param in
     * @return
     * @throws RedisException 
     */
    public String readBulkReply(InputStream in) throws RedisException;
    
    /**
     * Reads a Bulk Response from the provided stream and decodes
     * the response to a String using the specified Charset.
     * <p>
     * This method blocks until the entire response is received.
     * <p>
     * This is equivalent to:
     * <pre>new String(getBulkResponse(in), Charset.forName(charset));</pre>
     * 
     * @param in
     * @return
     * @throws RedisException 
     */
    public String readBulkReply(InputStream in, Charset charset) throws RedisException;
    
    /**
     * Gets a Multi-Bulk Response from the provided stream, returning
     * each Bulk Reply as an individual byte array.  The individual
     * responses appear in the response in the order received from Redis.
     * <p>
     * This method blocks until the entire response is received.
     * 
     * @param in
     * @return
     * @throws RedisException 
     */
    public byte[][] readMultiBulkReplyBinary(InputStream in) throws RedisException;
    
    /**
     * Reads a Multi-Bulk Response from the provided stream, and decodes
     * each Bulk Reply into a String using the default Charset (UTF-8).
     * The individual Bulk reply responses appear in the response in the
     * order received from Redis.
     * <p>
     * This method blocks until the entire response is received.
     * 
     * @param in
     * @return
     * @throws RedisException 
     */
    public String[] readMultiBulkReply(InputStream in) throws RedisException;
    
    /**
     * Reads a Multi-Bulk Response from the provided stream, and decodes
     * each Bulk Reply into a String using the default specified Charset.
     * The individual Bulk reply responses appear in the response in the
     * order received from Redis.
     * <p>
     * This method blocks until the entire response is received
     * 
     * @param in
     * @return
     * @throws RedisException 
     */
    public String[] readMultiBulkReply(InputStream in, Charset charset) throws RedisException;
}
