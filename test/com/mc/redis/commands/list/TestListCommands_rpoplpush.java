package com.mc.redis.commands.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_rpoplpush extends RedisClientTest {
    
    @Before public void reset() throws Exception {
        expectBulkReply();
    }
    
    @Test public void rpoplpush_default() throws Exception {
        assertBulkReply(client.rpoplpush(KEY1, KEY2));
        assertRequest("RPOPLPUSH", KEY1, KEY2);
    }
    
    @Test public void rpoplpush_encoded() throws Exception {
        assertBulkReply(client.rpoplpush(KEY1, KEY2, utf16), utf16);
        assertRequest("RPOPLPUSH", KEY1, KEY2);
    }
    
    @Test public void rpoplpush_binary() throws Exception {
        assertBulkReplyBinary(client.rpoplpushBytes(KEY1, KEY2));
        assertRequest("RPOPLPUSH", KEY1, KEY2);
    }
    
    @Test public void rpoplpush_null() throws Exception {
        expectNullReply();
        Assert.assertNull(client.rpoplpush(KEY1, KEY2));
    }
    
    @Test public void rpoplpush_error() throws Exception {
        expectExceptionReply();
        try {
            client.rpoplpush(KEY1, KEY2);
            Assert.fail("Exception expected");
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
}
