package com.mc.redis.commands.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_lpop_rpop extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectBulkReply();
    }
    
    @Test public void lpop_default() throws Exception {
        assertBulkReply(client.lpop(KEY));
        assertRequest("LPOP", KEY);
    }
    
    @Test public void lpop_encoded() throws Exception {
        assertBulkReply(client.lpop(KEY, utf16), utf16);
        assertRequest("LPOP", KEY);
    }
    
    @Test public void lpop_binary() throws Exception {
        assertBulkReplyBinary(client.lpopBytes(KEY));
        assertRequest("LPOP", KEY);
    }
    
    @Test public void lpop_error() throws Exception {
        expectExceptionReply();
        try {
            client.lpop(KEY);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void lpop_null() throws Exception {
        expectNullReply();
        assertNullReply(client.lpop(KEY));
        
        expectNullReply();
        assertNullReply(client.lpop(KEY, utf16));
        
        expectNullReply();
        assertNullReply(client.lpopBytes(KEY));
    }
    
    @Test public void rpop_default() throws Exception {
        assertBulkReply(client.rpop(KEY));
        assertRequest("RPOP", KEY);
    }
    
    @Test public void rpop_encoded() throws Exception {
        assertBulkReply(client.rpop(KEY, utf16), utf16);
        assertRequest("RPOP", KEY);
    }
    
    @Test public void rpop_binary() throws Exception {
        assertBulkReplyBinary(client.rpopBytes(KEY));
        assertRequest("RPOP", KEY);
    }
    
    @Test public void rpop_error() throws Exception {
        expectExceptionReply();
        try {
            client.rpop(KEY);
            Assert.fail("Expected exception");
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void rpop_null() throws Exception {
        expectNullReply();
        assertNullReply(client.rpop(KEY));
        
        expectNullReply();
        assertNullReply(client.rpop(KEY, utf16));
        
        expectNullReply();
        assertNullReply(client.rpopBytes(KEY));
    }
}
