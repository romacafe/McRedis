package com.mc.redis.commands.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListComands_ltrim extends RedisClientTest {
    @Before public void reset() throws Exception {
    }
    
    @Test public void ltrim() throws Exception {
        expectSingleLineReply();
        client.ltrim(KEY, BEGIN, END);
        assertVoidResponse();
        assertRequest("LTRIM", KEY, BEGIN_STR, END_STR);
    }
    
    @Test public void ltrim_exception() throws Exception {
        expectExceptionReply();
        try {
            client.ltrim(KEY, BEGIN, END);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
}
