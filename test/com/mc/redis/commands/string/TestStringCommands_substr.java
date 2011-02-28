package com.mc.redis.commands.string;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_substr extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectBulkReply();
    }
    
    @Test public void substr() throws Exception {
        assertBulkReply(client.substr(KEY, 1, 2));
        assertRequest("SUBSTR", KEY, "1", "2");
    }
    
    @Test public void substr_encoded() throws Exception {
        assertBulkReply(client.substr(KEY, 1, 2, utf16), utf16);
        assertRequest("SUBSTR", KEY, "1", "2");
    }
    
    @Test public void substr_binary() throws Exception {
        assertBulkReplyBinary(client.substrBytes(KEY, 1, 2));
        assertRequest("SUBSTR", KEY, "1", "2");
    }
    
    @Test public void substr_null() throws Exception {
        expectNullReply();
        Assert.assertNull(client.substr(KEY, 1, 2));
    }
    
    @Test public void substr_exception() throws Exception {
        expectExceptionReply();
        try {
            client.substr(KEY, 1, 3);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
}
