package com.mc.redis;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;


public class RedisUniversalProtocolTest_Output extends RedisUniversalProtocolTest {
    private final String CMD = "COMMAND";
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    @Test
    public void testOutputBytes() throws Exception {
        rp.writeRequest(out, CMD, SPECIAL_VALUE1.getBytes(utf8), SPECIAL_VALUE2.getBytes(utf16));
        byte[] actual = out.toByteArray();
        byte[] expected = buildRedisRequest(CMD, SPECIAL_VALUE1.getBytes(utf8), SPECIAL_VALUE2.getBytes(utf16));
        
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testOutputStringDefault() throws Exception {
        rp.writeRequest(out, CMD, SPECIAL_VALUE1, SPECIAL_VALUE2);
        byte[] actual = out.toByteArray();
        byte[] expected = buildRedisRequest(CMD, SPECIAL_VALUE1.getBytes(utf8), SPECIAL_VALUE2.getBytes(utf8));
        
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test(expected=NullPointerException.class)
    public void testOutputWithNullCommand() throws Throwable {
        try {
            rp.writeRequest(out, null, SPECIAL_VALUE1);
            Assert.fail("Expected a McRedisException");
        } catch (RedisException e) {
            Assert.assertEquals("Command cannot be null in a request to Redis", e.getMessage());
            throw e.getCause();
        }
    }
    
    @Test
    public void testOutputWithNullArgument() throws Exception {
        rp.writeRequest(out, CMD, (String)null, "MSG");
        byte[] actual = out.toByteArray();
        byte[] expected = "*3\r\n$7\r\nCOMMAND\r\n$-1\r\n$3\r\nMSG\r\n".getBytes(utf8);
        
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testOuputWithNullArgumentList() throws Exception {
        rp.writeRequest(out, CMD, (String[])null);
        byte[] actual = out.toByteArray();
        byte[] expected = "*1\r\n$7\r\nCOMMAND\r\n".getBytes(utf8);
        
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testOutputWithNoArguments() throws Exception {
        rp.writeRequest(out, CMD);
        byte[] actual = out.toByteArray();
        byte[] expected = "*1\r\n$7\r\nCOMMAND\r\n".getBytes(utf8);
        
        Assert.assertArrayEquals(expected, actual);
    }
}
