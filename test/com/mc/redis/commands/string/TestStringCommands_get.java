package com.mc.redis.commands.string;

import org.junit.Assert;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_get extends RedisClientTest {
    @Test public void get_default() throws Exception {
        expectBulkReply();
        assertBulkReply(client.get(KEY));
        assertRequest("GET", KEY);
    }
    
    @Test public void get_encoded() throws Exception {
        expectBulkReply();
        assertBulkReply(client.get(KEY, utf16), utf16);
        assertRequest("GET", KEY);
    }
    
    @Test public void get_binary() throws Exception {
        expectBulkReply();
        assertBulkReplyBinary(client.getBytes(KEY));
        assertRequest("GET", KEY);
    }
    
    @Test public void get_exception() throws Exception {
        expectExceptionReply();
        try {
            client.get(KEY);
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void get_null() throws Exception {
        expectNullReply();
        Assert.assertNull(client.get(KEY));
        assertRequest("GET", KEY);
    }
}
