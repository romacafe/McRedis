package com.mc.redis.commands.string;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_set extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectSingleLineReply();
    }
    
    @Test public void set_default() throws Exception {
        client.set(KEY, SPECIAL_VALUE1);
        assertVoidResponse();
        assertRequest("SET", KEY, SPECIAL_VALUE1);
    }
    
    @Test public void set_encoded() throws Exception {
        client.set(KEY, SPECIAL_VALUE1, utf16);
        assertVoidResponse();
        assertRequest("SET", KEY.getBytes(utf8), SPECIAL_VALUE1.getBytes(utf16));
    }

    @Test public void set_bytes() throws Exception {
        client.setBytes(KEY, SPECIAL_VALUE1.getBytes(utf16));
        assertVoidResponse();
        assertRequest("SET", KEY.getBytes(utf8), SPECIAL_VALUE1.getBytes(utf16));
    }
    
    @Test public void set_exception() throws Exception {
        expectExceptionReply();
        try {
            client.set(KEY, "VALUE");
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void set_null() throws Exception {
        client.set(KEY, null);
        assertRequest("SET", KEY, null);

        reset();
        client.set(KEY, null, utf16);
        assertRequest("SET", KEY, null);
        
        reset();
        client.setBytes(KEY, null);
        assertRequest("SET", KEY, null);
    }
}
