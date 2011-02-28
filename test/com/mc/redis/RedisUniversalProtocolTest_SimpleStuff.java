package com.mc.redis;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

public class RedisUniversalProtocolTest_SimpleStuff extends RedisUniversalProtocolTest {
    
    @Test
    public void testSingleLineResponse() throws Exception {
        runSingleLineTest("message");
        runSingleLineTest("I am a message with \r control \n characters");
    }
    
    @Test(expected=RedisErrorResponse.class)
    public void testSingleLineErrorResponse() throws Exception {
        String errorResp = "-something\r\n";
        InputStream in = new ByteArrayInputStream(errorResp.getBytes(utf8));
        rp.readSingleLineReply(in);
    }
    
    @Test
    public void testSmallInt() throws Exception {
        byte[] bytes = createSimpleMessage(ResponseType.NUMBER, "123");
        InputStream in = new ByteArrayInputStream(bytes);
        int resp = rp.readSmallInt(in);
        Assert.assertEquals(123, resp);
    }
    
    @Test
    public void testBigInt() throws Exception {
        byte[] bytes = createSimpleMessage(ResponseType.NUMBER, Integer.MAX_VALUE + "00");
        InputStream in = new ByteArrayInputStream(bytes);
        BigInteger resp = rp.readBigInt(in);
        
        BigInteger expected = new BigInteger(Integer.MAX_VALUE + "00");
        
        Assert.assertEquals(expected, resp);
    }
    
    @Test
    public void testLong() throws Exception {
        byte[] bytes = createSimpleMessage(ResponseType.NUMBER, Integer.MAX_VALUE + "00");
        InputStream in = new ByteArrayInputStream(bytes);
        long resp = rp.readLong(in);
        
        long expected = new BigInteger(Integer.MAX_VALUE + "00").longValue();
        
        Assert.assertEquals(expected, resp);
    }
    
    @Test(expected=NumberFormatException.class)
    public void testIntNumberFormatException() throws Throwable {
        byte[] bytes = createSimpleMessage(ResponseType.NUMBER, "abc");
        InputStream in = new ByteArrayInputStream(bytes);
        try {
            rp.readSmallInt(in);
        } catch (RedisException e) {
            throw e.getCause();
        }
    }
    
    @Test(expected=RedisErrorResponse.class)
    public void testIntErrorResponse() throws Exception {
        byte[] bytes = "-error\r\n".getBytes(utf8);
        InputStream in = new ByteArrayInputStream(bytes);
        rp.readSmallInt(in);
    }
    
    @Test
    public void testBoolean() throws Exception {
        byte[] bytes = createSimpleMessage(ResponseType.NUMBER, "1");
        InputStream in = new ByteArrayInputStream(bytes);
        Assert.assertTrue(rp.readBoolean(in));
        
        bytes = createSimpleMessage(ResponseType.NUMBER, "0");
        in = new ByteArrayInputStream(bytes);
        Assert.assertFalse(rp.readBoolean(in));
    }
    
    @Test(expected=RedisException.class)
    public void testBooleanError() throws Exception {
        byte[] bytes = createSimpleMessage(ResponseType.NUMBER, "3");
        InputStream in = new ByteArrayInputStream(bytes);
        rp.readBoolean(in);
    }

    private void runSingleLineTest(String msg) {
        byte[] bytes = createSimpleMessage(ResponseType.SINGLE_LINE, msg);
        InputStream in = new ByteArrayInputStream(bytes);
        String resp = rp.readSingleLineReply(in);
        Assert.assertEquals(msg, resp);
    }

    private byte[] createSimpleMessage(ResponseType type, String msg) {
        String singleLineResponse = type.indicator + msg + "\r\n";
        return singleLineResponse.getBytes(utf8);
    }
}
