package com.mc.redis.commands.string;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_msetnx extends RedisClientTest {
    @Before public void rest() throws Exception {
        expectBooleanReply(true);
    }
    
    @Test public void msetnx_default() throws Exception {
        assertBooleanReply(client.msetnx(KEY1, VALUE1, KEY2, VALUE2), true);
        assertRequest("MSETNX", KEY1, VALUE1, KEY2, VALUE2);
    }
    
    @Test public void msetnx_encoded() throws Exception {
        assertBooleanReply(client.msetnx(utf16, KEY1, VALUE1, KEY2, VALUE2), true);
        assertRequest("MSETNX", utf8(KEY1), utf16(VALUE1), utf8(KEY2), utf16(VALUE2));
    }
    
    @Test public void msetnx_exception() throws Exception {
        expectExceptionReply();
        try {
            client.msetnx(KEY1, VALUE1, KEY2, VALUE2);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void msetnx_null() throws Exception {
        assertBooleanReply(client.msetnx(KEY1, VALUE1, KEY2, null, KEY3, null), true);
        assertRequest("MSETNX", KEY1, VALUE1, KEY2, null, KEY3, null);
    }
    
}
