package com.mc.redis.commands.list;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_lrem extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectIntegerReply();
    }
    
    @Test public void lrem_default() throws Exception {
        assertIntegerReply(client.lrem(KEY, COUNT, VALUE));
        assertRequest("LREM", KEY, COUNT_STR, VALUE);
    }
    
    @Test public void lrem_encoded() throws Exception {
        assertIntegerReply(client.lrem(KEY, COUNT, VALUE, utf16));
        assertRequest("LREM", utf8(KEY), utf8(COUNT_STR), utf16(VALUE));
    }
    
    @Test public void lrem_binary() throws Exception {
        assertIntegerReply(client.lremBytes(KEY, COUNT, utf16(VALUE)));
        assertRequest("LREM", utf8(KEY), utf8(COUNT_STR), utf16(VALUE));
    }
    
    @Test public void lrem_null() throws Exception {
        client.lrem(KEY, COUNT, null);
        assertRequest("LREM", KEY, COUNT, null);
        
        reset();
        client.lrem(KEY, COUNT, null, utf16);
        assertRequest("LREM", KEY, COUNT, null);
        
        reset();
        client.lremBytes(KEY, COUNT, null);
        assertRequest("LREM", KEY, COUNT, null);
    }
    
    @Test public void lpush_exception() throws Exception {
        expectExceptionReply();
        try {
            client.lrem(KEY, COUNT, VALUE);
            Assert.fail("Expected an exception");
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
}
