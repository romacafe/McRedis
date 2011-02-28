package com.mc.redis.commands.list;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_push extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectIntegerReply();
    }
    
    @Test public void lpush_default() throws Exception {
        assertIntegerReply(client.lpush(KEY, VALUE));
        assertRequest("LPUSH", KEY, VALUE);
    }
    
    @Test public void lpush_encoded() throws Exception {
        assertIntegerReply(client.lpush(KEY, VALUE, utf16));
        assertRequest("LPUSH", utf8(KEY), utf16(VALUE));
    }
    
    @Test public void lpush_binary() throws Exception {
        assertIntegerReply(client.lpushBytes(KEY, utf16(VALUE)));
        assertRequest("LPUSH", utf8(KEY), utf16(VALUE));
    }
    
    @Test public void lpush_null() throws Exception {
        assertIntegerReply(client.lpushBytes(KEY, null));
        assertRequest("LPUSH", KEY, null);
    }
    
    @Test public void lpush_exception() throws Exception {
        expectExceptionReply();
        try {
            client.lpush(KEY, VALUE);
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void rpush_default() throws Exception {
        assertIntegerReply(client.rpush(KEY, VALUE));
        assertRequest("RPUSH", KEY, VALUE);
    }
    
    @Test public void rpush_encoded() throws Exception {
        assertIntegerReply(client.rpush(KEY, VALUE, utf16));
        assertRequest("RPUSH", utf8(KEY), utf16(VALUE));
    }
    
    @Test public void rpush_binary() throws Exception {
        assertIntegerReply(client.rpushBytes(KEY, utf16(VALUE)));
        assertRequest("RPUSH", utf8(KEY), utf16(VALUE));
    }
    
    @Test public void rpush_null() throws Exception {
        assertIntegerReply(client.rpushBytes(KEY, null));
        assertRequest("RPUSH", KEY, null);
    }
    
    @Test public void rpush_exception() throws Exception {
        expectExceptionReply();
        try {
            client.rpush(KEY, VALUE);
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
}
