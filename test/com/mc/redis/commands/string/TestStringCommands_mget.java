package com.mc.redis.commands.string;

import org.junit.Assert;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_mget extends RedisClientTest {
    private static final String KEY1 = "KEY1";
    
    @Test public void mget_default() throws Exception {
        expectMultiBulkReply();
        assertMultiBulkReply(client.mget(KEY, KEY1));
        assertRequest("MGET", KEY, KEY1);
    }
    
    @Test public void mget_encoded() throws Exception {
        expectMultiBulkReply();
        assertMultiBulkReply(client.mget(utf16, KEY, KEY1), utf16);
        assertRequest("MGET", KEY, KEY1);
    }
    
    @Test public void mget_binary() throws Exception {
        expectMultiBulkReply();
        assertMultiBulkReplyBinary(client.mgetBytes(KEY, KEY1));
        assertRequest("MGET", KEY, KEY1);
    }
    
    @Test public void mget_exception() throws Exception {
        expectExceptionReply();
        try {
            client.mget(KEY, KEY1);
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void mget_null() throws Exception {
        expectMultiBulkReplyWithNull();
        Assert.assertNull(client.mget(KEY, KEY1)[0]);
    }
}
