package com.mc.redis.commands.string;

import org.junit.Assert;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_getset extends RedisClientTest {
    @Test public void getset_default() throws Exception {
        expectBulkReply();
        assertBulkReply(client.getset(KEY, "VALUE"));
        assertRequest("GETSET", "KEY", "VALUE");
    }
    
    @Test public void getset_encoded() throws Exception {
        expectBulkReply();
        assertBulkReply(client.getset(KEY, "VALUE", utf16), utf16);
        assertRequest("GETSET", KEY.getBytes(utf8), "VALUE".getBytes(utf16));
    }
    
    @Test public void getset_binary() throws Exception {
        expectBulkReply();
        assertBulkReplyBinary(client.getsetBytes(KEY, SPECIAL_VALUE1.getBytes(utf16)));
        assertRequest("GETSET", KEY.getBytes(utf8), SPECIAL_VALUE1.getBytes(utf16));
    }
    
    @Test public void getset_exception() throws Exception {
        expectExceptionReply();
        try {
            client.getset(KEY, "VALUE");
            Assert.fail("Expected Exception");
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void getset_null() throws Exception {
        expectNullReply();
        Assert.assertNull(client.getset(KEY, "VALUE"));
        assertRequest("GETSET", KEY, "VALUE");
    }
}
