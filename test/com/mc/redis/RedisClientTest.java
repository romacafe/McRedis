package com.mc.redis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;

import com.mc.redis.mock.MockClient;

public abstract class RedisClientTest extends RedisUniversalProtocolTest {
    protected RedisClient client;
    private RedisUniversalProtocol protocol;
    
    @Before
    public void createClient() throws Exception {
        client = new MockClient();
        protocol = new RedisUniversalProtocol();
    }
    
    protected byte[] utf8(String s) {
        return s.getBytes(utf8);
    }
    
    protected byte[] utf16(String s) {
        return s.getBytes(utf16);
    }
    
    protected void assertRequest(String command) {
        assertRequest(new String[]{command});
    }

    protected void assertRequest(String... params) {
        String comamnd = params[0];
        byte[][] paramBytes = new byte[params.length-1][];
        for (int i=1; i<params.length; i++) {
            paramBytes[i-1] = params[i] == null ? null : params[i].getBytes(utf8);
        }
        assertRequest(comamnd, paramBytes);
    }
    
    protected void assertRequest(Object... params) {
        String[] stringParams = new String[params.length];
        for (int i=0; i<params.length; i++) {
            stringParams[i] = params[i] == null ? null : params[i].toString();
        }
        assertRequest(stringParams);
    }
    
    protected void assertRequest(String command, byte[]... params) {
        String expectedRequest = requestForParams(command, params);
        String s = new String(((MockClient)client).getOutputStream().toByteArray(), utf8);
        Assert.assertEquals(expectedRequest, s);
    }

    protected String requestForParams(String command, byte[]... params) {
        try {
            return new String(buildRedisRequest(command, params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void assertVoidResponse() throws Exception {
        Assert.assertTrue("Methods returning void are still " +
        		"expected to read the single-line reply from Redis",
                0 == ((MockClient)client).in.available());
    }
    
    protected void assertBooleanReply(boolean actual, boolean expected) {
        Assert.assertTrue("Expected " + actual + ", got " + expected, actual == expected);
    }

    protected void assertBulkReply(String actual) throws Exception {
        assertBulkReply(actual, utf8);
    }
    
    protected void assertBulkReply(String actual, Charset charset) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(BULK_RESPONSE);
        String expected = protocol.readBulkReply(bis, charset);
        Assert.assertEquals(expected, actual);
    }
    
    protected void assertBulkReplyBinary(byte[] actual) {
        ByteArrayInputStream bis = new ByteArrayInputStream(BULK_RESPONSE);
        byte[] expected = protocol.readBulkReplyBinary(bis);
        Assert.assertArrayEquals(expected, actual);
    }

    protected void assertMultiBulkReply(String[] actual) throws Exception {
        assertMultiBulkReply(actual, utf8);
    }

    protected void assertMultiBulkReply(String[] actual, Charset charset) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(MULTI_BULK_RESPONSE);
        String[] expected = protocol.readMultiBulkReply(bis, charset);
        Assert.assertArrayEquals(expected, actual);
    }
    
    protected void assertMultiBulkReply_specifyEncodings(String[] actual, Charset... encodings) throws Exception {
        Assert.assertEquals(encodings.length, actual.length);
        ByteArrayInputStream bis = new ByteArrayInputStream(MULTI_BULK_RESPONSE);
        byte[][] expected = protocol.readMultiBulkReplyBinary(bis);
        for (int i=0; i<encodings.length; i++) {
            String expectedString = new String(expected[i], encodings[i]);
            Assert.assertEquals(expectedString, actual[i]);
        }
    }
    
    protected void assertMultiBulkReplyBinary(byte[][] actual) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(MULTI_BULK_RESPONSE);
        byte[][] expected = protocol.readMultiBulkReplyBinary(bis);
        Assert.assertArrayEquals(expected, actual);
    }
    
    public void assertNullReply(Object actual) throws Exception {
        Assert.assertNull(actual);
    }

    protected void assertSingleLineReply(String actual) {
        ByteArrayInputStream bis = new ByteArrayInputStream(STATUS_RESPONSE);
        String expected = protocol.readSingleLineReply(bis);
        Assert.assertEquals(expected, actual);
    }

    protected void assertExceptionReply(Exception e) {
        Assert.assertTrue(RedisException.class.isAssignableFrom(e.getClass()));
        Assert.assertEquals(ERROR_MESSAGE, e.getMessage());
    }
    
    protected void assertIntegerReply(long actual) {
        Assert.assertEquals(DEFAULT_INTEGER_VALUE, actual);
    }
    
    protected void expectNoReply() throws Exception {
        programClientForResponse(new ByteArrayInputStream(new byte[0]));
    }
    
    protected void expectNullReply() throws Exception {
        programClientForResponse(new ByteArrayInputStream(NULL_BULK_RESPONSE));
    }
    
    protected void expectMultiBulkReplyWithNull() throws Exception {
        programClientForResponse(new ByteArrayInputStream(NULL_MUTLI_BULK_RESPONSE));
    }
    
    protected void expectSingleLineReply() throws Exception {
        programClientForResponse(new ByteArrayInputStream(STATUS_RESPONSE));
    }
    
    protected void expectIntegerReply() throws Exception {
        programClientForResponse(new ByteArrayInputStream((":" + DEFAULT_INTEGER_VALUE + "\r\n").getBytes(utf8)));
    }
    
    protected void expectLongReply(long value) throws Exception {
        programClientForResponse(new ByteArrayInputStream((":" + value + "\r\n").getBytes(utf8)));
    }
    
    protected void expectExceptionReply() throws Exception {
        programClientForResponse(new ByteArrayInputStream(ERROR_RESPONSE));
    }

    protected void expectBulkReply() throws Exception {
        programClientForResponse(new ByteArrayInputStream(BULK_RESPONSE));
    }

    protected void expectMultiBulkReply() throws Exception {
        programClientForResponse(new ByteArrayInputStream(MULTI_BULK_RESPONSE));
    }
    
    protected void expectBooleanReply(boolean returnTrue) throws Exception {
        if (returnTrue) {
            programClientForResponse(new ByteArrayInputStream(INTEGER_RESPONSE_ONE));
        } else {
            programClientForResponse(new ByteArrayInputStream(INTEGER_RESPONSE_ZERO));
        }
    }

    protected void programClientForResponse(ByteArrayInputStream in)
            throws IOException {
        ((MockClient)client).programForResponse(in);
    }

    private static final byte[] NULL_BULK_RESPONSE =
        "$-1\r\n".getBytes(utf8);
    private static final byte[] NULL_MUTLI_BULK_RESPONSE = 
        ("*2\r\n" + new String(NULL_BULK_RESPONSE, utf8) + "$2\r\nHI\r\n").getBytes(utf8);
    
    private static final byte[] BULK_RESPONSE =
        "$28\r\nTHIS IS A FAKE BULK RESPONSE\r\n".getBytes(utf8);
    private static final byte[] MULTI_BULK_RESPONSE =
        "*2\r\n$5\r\nHELLO\r\n$5\r\nWORLD\r\n".getBytes(utf8);
    private static final byte[] STATUS_RESPONSE =
        "+STATUS RESPONSE HERE\r\n".getBytes(utf8);
    private static final String ERROR_MESSAGE = "ERROR MESSAGE";
    private static final byte[] ERROR_RESPONSE =
        ("-" + ERROR_MESSAGE + "\r\n").getBytes(utf8);
    private static final byte[] INTEGER_RESPONSE_ONE =
        ":1\r\n".getBytes(utf8);
    private static final byte[] INTEGER_RESPONSE_ZERO =
        ":0\r\n".getBytes(utf8);

    private static final int DEFAULT_INTEGER_VALUE = 962;

    protected static final String KEY = "KEY";
    protected static final String KEY1 = "KEY1";
    protected static final String KEY2 = "KEY2";
    protected static final String KEY3 = "KEY3";
    protected static final String VALUE = "VALUE";
    protected static final String VALUE1= "VALUE1";
    protected static final String VALUE2= "VALUE2";
    protected static final String VALUE3= "VALUE3";
    
    protected static final int BEGIN = 3;
    protected static final int END = 34982;
    protected static final int COUNT = 99;
    protected static final String BEGIN_STR = String.valueOf(BEGIN);
    protected static final String END_STR = String.valueOf(END);
    protected static final String COUNT_STR = String.valueOf(COUNT);
        
}
