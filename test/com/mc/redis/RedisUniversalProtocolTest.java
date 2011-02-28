package com.mc.redis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Methods & Constants useful for testing RedisProtocol implementations
 * @author mcase
 *
 */
public abstract class RedisUniversalProtocolTest {

    protected RedisUniversalProtocol rp = new RedisUniversalProtocol();

    protected byte[] bulkResponseBytes(byte[] input)
            throws IOException {
                
                if (input == null) {
                    return (REDIS_NULL+REDIS_DELIM).getBytes(utf8);
                }
                
                byte[] inputBytes = input;
                
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bout.write(BULK_INDICATOR.getBytes(utf8));
                String len = String.valueOf(inputBytes.length);
                bout.write(len.getBytes(utf8));
                bout.write(REDIS_DELIM.getBytes(utf8));
                bout.write(inputBytes);
                bout.write(REDIS_DELIM.getBytes(utf8));
                byte[] bytes = bout.toByteArray();
                return bytes;
            }

    protected byte[] buildMultiBulkMessage(byte[]... input)
            throws IOException {
                int numParts = input.length;
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bout.write(MULTI_BULK_INDICATOR.getBytes(utf8));
                bout.write(String.valueOf(numParts).getBytes(utf8));
                bout.write(REDIS_DELIM.getBytes(utf8));
                for (int i=0; i<numParts; i++) {
                    bout.write(bulkResponseBytes(input[i]));
                }
                return bout.toByteArray();
            }
    
    protected byte[] buildRedisRequest(String cmd, byte[]... input) throws IOException {
        byte[][] newArgs = new byte[input == null ? 1 : input.length + 1][];
        newArgs[0] = cmd.getBytes(utf8);
        int i=1;
        if (null != input) {
            for (byte[] arg : input) {
                newArgs[i++] = arg;
            }
        }
        return buildMultiBulkMessage(newArgs);
    }

    protected static final Charset utf16 = Charset.forName("UTF-16BE");
    protected static final Charset utf8 = Charset.forName("UTF-8");
    protected static final String SPECIAL_VALUE1 = "ß©ƒ˙©†˙";
    protected static final String SPECIAL_VALUE2 = "∆©®¥†¬∫ƒ∂";
    protected static String REDIS_DELIM = "\r\n";
    protected static String REDIS_NULL = "$-1";
    protected static String BULK_INDICATOR = "$";
    protected static String MULTI_BULK_INDICATOR = "*";

    public RedisUniversalProtocolTest() {
        super();
    }

}