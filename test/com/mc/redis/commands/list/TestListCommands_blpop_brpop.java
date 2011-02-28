package com.mc.redis.commands.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_blpop_brpop extends RedisClientTest {
    
    @Before public void reset() throws Exception {
        expectMultiBulkReply();
    }
    
    @Test public void blpop_default() throws Exception {
        assertMultiBulkReply_specifyEncodings(client.blpop(COUNT, KEY1, KEY2, KEY3), utf8, utf8);
        assertRequest("BLPOP", KEY1, KEY2, KEY3, COUNT_STR);
    }
    
    @Test public void blpop_encoded() throws Exception {
        assertMultiBulkReply_specifyEncodings(client.blpop(COUNT, utf16, KEY1, KEY2, KEY3), utf8, utf16);
        assertRequest("BLPOP", KEY1, KEY2, KEY3, COUNT_STR);
    }
    
    @Test public void blpop_binary() throws Exception {
        assertMultiBulkReplyBinary(client.blpopBytes(COUNT, KEY1, KEY2, KEY3));
        assertRequest("BLPOP", KEY1, KEY2, KEY3, COUNT_STR);
    }
    
    @Test public void blpop_error() throws Exception {
        expectExceptionReply();
        try {
            client.blpop(COUNT, KEY);
            Assert.fail("Expected exception");
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void blpop_null() throws Exception {
        //TODO: Can't remember what this returns when the timeout happens, does it return null?
        //For now, assume it does, until I can get online...
        expectNullReply();
        assertNullReply(client.blpop(COUNT, KEY1, KEY2));
        
        expectNullReply();
        assertNullReply(client.blpop(COUNT, utf16, KEY1, KEY2, KEY3));
        
        expectNullReply();
        assertNullReply(client.blpopBytes(COUNT, KEY1, KEY2));
    }
    
    @Test public void brpop_default() throws Exception {
        assertMultiBulkReply_specifyEncodings(client.brpop(COUNT, KEY1, KEY2, KEY3), utf8, utf8);
        assertRequest("BRPOP", KEY1, KEY2, KEY3, COUNT_STR);
    }
    
    @Test public void brpop_encoded() throws Exception {
        assertMultiBulkReply_specifyEncodings(client.brpop(COUNT, utf16, KEY1, KEY2, KEY3), utf8, utf16);
        assertRequest("BRPOP", KEY1, KEY2, KEY3, COUNT_STR);
    }
    
    @Test public void brpop_binary() throws Exception {
        assertMultiBulkReplyBinary(client.brpopBytes(COUNT, KEY1, KEY2, KEY3));
        assertRequest("BRPOP", KEY1, KEY2, KEY3, COUNT_STR);
    }
    
    @Test public void brpop_error() throws Exception {
        expectExceptionReply();
        try {
            client.brpop(COUNT, KEY);
            Assert.fail("Expected exception");
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void brpop_null() throws Exception {
        expectNullReply();
        assertNullReply(client.brpop(COUNT, KEY1, KEY2));
        
        expectNullReply();
        assertNullReply(client.brpop(COUNT, utf16, KEY1, KEY2, KEY3));
        
        expectNullReply();
        assertNullReply(client.brpopBytes(COUNT, KEY1, KEY2));
    }
}
