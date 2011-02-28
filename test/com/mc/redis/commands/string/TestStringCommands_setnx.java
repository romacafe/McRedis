package com.mc.redis.commands.string;

import junit.framework.Assert;

import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_setnx extends RedisClientTest {
    @Test public void setnx_default() throws Exception {
        expectBooleanReply(true);
        assertBooleanReply(client.setnx(KEY, "VALUE"), true);
        assertRequest("SETNX", KEY, "VALUE");
    }
    
    @Test public void setnx_encoded() throws Exception {
        expectBooleanReply(false);
        assertBooleanReply(client.setnx(KEY, "VALUE", utf16), false);
        assertRequest("SETNX", KEY.getBytes(utf8), "VALUE".getBytes(utf16));
    }
    
    @Test public void setnx_bytes() throws Exception {
        expectBooleanReply(false);
        assertBooleanReply(client.setnxBytes(KEY, "VALUE".getBytes(utf16)), false);
        assertRequest("SETNX", KEY.getBytes(utf8), "VALUE".getBytes(utf16));
    }
    
    @Test public void setnx_exception() throws Exception {
        expectExceptionReply();
        try {
            client.setnx(KEY, "VALUE");
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void setnx_null() throws Exception {
        expectBooleanReply(true);
        assertBooleanReply(client.setnx(KEY, null), true);
        assertRequest("SETNX", KEY, null);
        
        expectBooleanReply(false);
        assertBooleanReply(client.setnx(KEY, null, utf16), false);
        assertRequest("SETNX", KEY, null);
        
        expectBooleanReply(false);
        assertBooleanReply(client.setnxBytes(KEY, null), false);
        assertRequest("SETNX", KEY, null);
    }
}
