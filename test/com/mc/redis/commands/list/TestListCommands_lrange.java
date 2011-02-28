package com.mc.redis.commands.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_lrange extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectMultiBulkReply();
    }
    
    @Test public void lrange_default() throws Exception {
        assertMultiBulkReply(client.lrange(KEY, BEGIN, END));
        assertRequest("LRANGE", KEY, BEGIN_STR, END_STR);
    }
    
    @Test public void lrange_encoded() throws Exception {
        assertMultiBulkReply(client.lrange(KEY, utf16, BEGIN, END), utf16);
        assertRequest("LRANGE", KEY, BEGIN_STR, END_STR);
    }
    
    @Test public void lrange_binary() throws Exception {
        assertMultiBulkReplyBinary(client.lrangeBytes(KEY, BEGIN, END));
        assertRequest("LRANGE", KEY, BEGIN_STR, END_STR);
    }
    
    @Test public void lrange_null() throws Exception {
        expectMultiBulkReplyWithNull();
        Assert.assertNull(client.lrange(KEY, BEGIN, END)[0]);
    }
    
    @Test public void lrange_exception() throws Exception {
        expectExceptionReply();
        try {
            client.lrange(KEY, BEGIN, END);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
}
