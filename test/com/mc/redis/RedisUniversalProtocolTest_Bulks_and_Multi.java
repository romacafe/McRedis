package com.mc.redis;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.mc.redis.exception.UnexpectedBytesException;
import com.mc.redis.exception.UnexpectedEndOfStreamException;


public class RedisUniversalProtocolTest_Bulks_and_Multi extends RedisUniversalProtocolTest {
    @Test
    public void testBulkResponseBytes() throws Exception {
        byte[] bytes = bulkResponseBytes(SPECIAL_VALUE1.getBytes(utf8));
        InputStream in = new ByteArrayInputStream(bytes);
        byte[] resp = rp.readBulkReplyBinary(in);
        
        Assert.assertArrayEquals(SPECIAL_VALUE1.getBytes(utf8), resp);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testBulkResponseBytesNull() throws Exception {
        byte[] bytes = bulkResponseBytes(null);
        InputStream in = new ByteArrayInputStream(bytes);
        byte[] resp = rp.readBulkReplyBinary(in);
        
        Assert.assertNull(resp);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testBulkResponseStringDefault() throws Exception {
        byte[] bytes = bulkResponseBytes(SPECIAL_VALUE1.getBytes(utf8));
        InputStream in = new ByteArrayInputStream(bytes);
        String resp = rp.readBulkReply(in);
        
        Assert.assertEquals(SPECIAL_VALUE1, resp);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testBulkResponseStringDefaultNull() throws Exception {
        byte[] bytes = bulkResponseBytes(null);
        InputStream in = new ByteArrayInputStream(bytes);
        String resp = rp.readBulkReply(in);
        
        Assert.assertNull(resp);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testBulkResponseStringSpecifiedCharset() throws Exception {
        byte[] bytes = bulkResponseBytes(SPECIAL_VALUE1.getBytes(utf16));
        InputStream in = new ByteArrayInputStream(bytes);
        String resp = rp.readBulkReply(in, utf16);
        
        Assert.assertEquals(SPECIAL_VALUE1, resp);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testMultiBulkResponseBytes() throws Exception {
        byte[] msg = buildMultiBulkMessage(SPECIAL_VALUE1.getBytes(utf8), SPECIAL_VALUE2.getBytes(utf8));
        InputStream in = new ByteArrayInputStream(msg);
        byte[][] resps = rp.readMultiBulkReplyBinary(in);
        
        Assert.assertEquals(2, resps.length);
        Assert.assertArrayEquals(SPECIAL_VALUE1.getBytes(utf8), resps[0]);
        Assert.assertArrayEquals(SPECIAL_VALUE2.getBytes(utf8), resps[1]);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testMultiBulkResponseBytesWithNull() throws Exception {
        byte[] msg = buildMultiBulkMessage(SPECIAL_VALUE1.getBytes(utf8), (byte[])null, SPECIAL_VALUE2.getBytes(utf8));
        InputStream in = new ByteArrayInputStream(msg);
        byte[][] resps = rp.readMultiBulkReplyBinary(in);
        
        Assert.assertEquals(3, resps.length);
        Assert.assertArrayEquals(SPECIAL_VALUE1.getBytes(utf8), resps[0]);
        Assert.assertNull(null, resps[1]);
        Assert.assertArrayEquals(SPECIAL_VALUE2.getBytes(utf8), resps[2]);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testMultiBulkResponseBytesWholeResponseIsNull() throws Exception {
        byte[] msg = bulkResponseBytes(null);
        InputStream in = new ByteArrayInputStream(msg);
        
        Object actual = rp.readMultiBulkReply(in);
        Assert.assertNull(actual);
        Assert.assertEquals(-1, in.read());
        
        in.reset();
        actual = rp.readMultiBulkReply(in, utf16);
        Assert.assertNull(actual);
        Assert.assertEquals(-1, in.read());
        
        in.reset();
        actual = rp.readMultiBulkReplyBinary(in);
        Assert.assertNull(actual);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testMultiBulkResponseStringDefault() throws Exception {
        byte[] msg = buildMultiBulkMessage(SPECIAL_VALUE1.getBytes(utf8), SPECIAL_VALUE2.getBytes(utf8));
        InputStream in = new ByteArrayInputStream(msg);
        String[] resps = rp.readMultiBulkReply(in);
        
        Assert.assertEquals(2, resps.length);
        Assert.assertEquals(SPECIAL_VALUE1, resps[0]);
        Assert.assertEquals(SPECIAL_VALUE2, resps[1]);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testMultiBulkResponseStringDefaultNull() throws Exception {
        byte[] msg = buildMultiBulkMessage(SPECIAL_VALUE1.getBytes(utf8), (byte[])null, SPECIAL_VALUE2.getBytes(utf8));
        InputStream in = new ByteArrayInputStream(msg);
        String[] resps = rp.readMultiBulkReply(in);
        
        Assert.assertEquals(3, resps.length);
        Assert.assertEquals(SPECIAL_VALUE1, resps[0]);
        Assert.assertNull(null, resps[1]);
        Assert.assertEquals(SPECIAL_VALUE2, resps[2]);
        Assert.assertEquals(-1, in.read());
    }

    @Test
    public void testMultiBulkResponseStringSpecifiedCharset() throws Exception {
        byte[] msg = buildMultiBulkMessage(SPECIAL_VALUE1.getBytes(utf16), SPECIAL_VALUE2.getBytes(utf16));
        InputStream in = new ByteArrayInputStream(msg);
        String[] resps = rp.readMultiBulkReply(in, utf16);
        
        Assert.assertEquals(2, resps.length);
        Assert.assertEquals(SPECIAL_VALUE1, resps[0]);
        Assert.assertEquals(SPECIAL_VALUE2, resps[1]);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testMultiBulkResponseStringSpecifiedCharsetNull() throws Exception {
        byte[] msg = buildMultiBulkMessage(SPECIAL_VALUE1.getBytes(utf16), (byte[])null, SPECIAL_VALUE2.getBytes(utf16));
        InputStream in = new ByteArrayInputStream(msg);
        String[] resps = rp.readMultiBulkReply(in, utf16);
        
        Assert.assertEquals(3, resps.length);
        Assert.assertEquals(SPECIAL_VALUE1, resps[0]);
        Assert.assertNull(resps[1]);
        Assert.assertEquals(SPECIAL_VALUE2, resps[2]);
        Assert.assertEquals(-1, in.read());
    }
    
    @Test
    public void testMultiBulkWithNil() throws Exception {
        
    }
    
    @Test(expected=UnexpectedEndOfStreamException.class)
    public void testIncompleteBulkResponseBytes() throws Exception {
        byte[] bytes = bulkResponseBytes(SPECIAL_VALUE1.getBytes(utf8));
        InputStream in = new ByteArrayInputStream(bytes, 0, bytes.length-10);
        rp.readBulkReplyBinary(in);
    }
    
    @Test(expected=UnexpectedEndOfStreamException.class)
    public void testIncompleteBulkResponseString() throws Exception {
        byte[] bytes = bulkResponseBytes(SPECIAL_VALUE1.getBytes(utf8));
        InputStream in = new ByteArrayInputStream(bytes, 0, bytes.length-10);
        rp.readBulkReply(in);
    }
    
    @Test(expected=UnexpectedBytesException.class)
    public void testBulkResponseBytesWithExtraBytesInResponse() throws Exception {
        byte[] bytes = bulkResponseBytes(SPECIAL_VALUE1.getBytes(utf8));
        //replace the ending /r/n with 'hi'
        bytes[bytes.length-1] = 'h';
        bytes[bytes.length-2] = 'i';
        InputStream in = new ByteArrayInputStream(bytes);
        rp.readBulkReply(in);
    }
    
    @Test(expected=RedisErrorResponse.class)
    public void testBulkResponseErrorResponse() throws Exception {
        String errorResponse = "-Error Text Here.\r\n";
        byte[] bytes = errorResponse.getBytes(utf8);
        InputStream in = new ByteArrayInputStream(bytes);
        rp.readBulkReply(in);
    }
    
    @Test(expected=RedisErrorResponse.class)
    public void testMultiBulkResponseErrorResponse() throws Exception {
        String errorResponse = "-Error Text Here.\r\n";
        byte[] bytes = errorResponse.getBytes(utf8);
        InputStream in = new ByteArrayInputStream(bytes);
        rp.readMultiBulkReply(in);
    }
}