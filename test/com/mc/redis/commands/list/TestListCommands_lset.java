package com.mc.redis.commands.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_lset extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectSingleLineReply();
    }
    
    @Test public void lset_default() throws Exception {
        client.lset(KEY, BEGIN, VALUE);
        assertVoidResponse();
        assertRequest("LSET", KEY, BEGIN_STR, VALUE);
    }
    
    @Test public void lset_encoded() throws Exception {
        client.lset(KEY, BEGIN, VALUE, utf16);
        assertVoidResponse();
        assertRequest("LSET", KEY.getBytes(utf8), BEGIN_STR.getBytes(utf8), VALUE.getBytes(utf16));
    }
    
    @Test public void lset_binary() throws Exception {
        client.lsetBytes(KEY, BEGIN, VALUE.getBytes(utf16));
        assertVoidResponse();
        assertRequest("LSET", KEY.getBytes(utf8), BEGIN_STR.getBytes(utf8), VALUE.getBytes(utf16));
    }
    
    @Test public void lset_exception() throws Exception {
        expectExceptionReply();
        try {
            client.lset(KEY, BEGIN, VALUE);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void lset_null() throws Exception {
        client.lset(KEY, BEGIN, null);
        assertRequest("LSET", "KEY".getBytes(utf8), BEGIN_STR.getBytes(utf8), null);
        
        reset();
        client.lset(KEY, BEGIN, null, utf16);
        assertRequest("LSET", "KEY".getBytes(utf8), BEGIN_STR.getBytes(utf8), null);
        
        reset();
        client.lsetBytes(KEY, BEGIN, null);
        assertRequest("LSET", "KEY".getBytes(utf8), BEGIN_STR.getBytes(utf8), null);
    }
}
