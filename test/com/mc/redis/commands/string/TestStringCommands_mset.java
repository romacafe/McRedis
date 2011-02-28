package com.mc.redis.commands.string;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_mset extends RedisClientTest {
    private static final String MSET = "MSET";
    
    @Before public void reset() throws Exception {
        expectSingleLineReply();
    }
    
    @Test public void mset_default() throws Exception {
        client.mset(KEY1, VALUE1, KEY2, VALUE2, KEY3, VALUE3);
        assertVoidResponse();
        assertRequest(MSET, utf8(KEY1), utf8(VALUE1),
                utf8(KEY2), utf8(VALUE2),
                utf8(KEY3), utf8(VALUE3));
    }
    
    @Test public void mset_encoded() throws Exception {
        client.mset(utf16, KEY1, VALUE1, KEY2, VALUE2, KEY3, VALUE3);
        assertVoidResponse();
        assertRequest(MSET, utf8(KEY1), utf16(VALUE1),
                utf8(KEY2), utf16(VALUE2),
                utf8(KEY3), utf16(VALUE3));
    }
    
    @Test public void mset_exception() throws Exception {
        expectExceptionReply();
        try {
            client.mset(KEY1, VALUE1, KEY2, VALUE2);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void mset_null() throws Exception {
        client.mset(KEY1, VALUE1, KEY2, null, KEY3, VALUE3);
        assertRequest(MSET, KEY1, VALUE1, KEY2, null, KEY3, VALUE3);
    }
}
