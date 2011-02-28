package com.mc.redis.commands.string;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_append extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectIntegerReply();
    }
    
    @Test public void append_default() throws Exception {
        assertIntegerReply(client.append(KEY, VALUE));
        assertRequest("APPEND", KEY, VALUE);
    }
    
    @Test public void append_encoded() throws Exception {
        assertIntegerReply(client.append(KEY, VALUE, utf16));
        assertRequest("APPEND", utf8(KEY), utf16(VALUE));
    }
    
    @Test public void append_binary() throws Exception {
        assertIntegerReply(client.appendBytes(KEY, VALUE.getBytes(utf16)));
        assertRequest("APPEND", utf8(KEY), utf16(VALUE));
    }
    
    @Test public void append_exception() throws Exception {
        expectExceptionReply();
        try {
            client.append(KEY, VALUE);
        } catch (Exception e) {
            assertExceptionReply(e);
        }
        
    }
    
    @Test public void append_null() throws Exception {
        assertIntegerReply(client.append(KEY, null));
        assertRequest("APPEND", KEY, null);
    }
}
