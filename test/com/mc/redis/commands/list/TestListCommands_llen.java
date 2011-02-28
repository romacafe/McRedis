package com.mc.redis.commands.list;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_llen extends RedisClientTest {
    
    @Before public void reset() throws Exception {
        expectIntegerReply();
    }
    
    @Test public void llen_default() throws Exception {
        assertIntegerReply(client.llen(KEY));
        assertRequest("LLEN", KEY);
    }
    
    @Test public void llen_error() throws Exception {
        expectExceptionReply();
        try {
            client.llen(KEY);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
}
