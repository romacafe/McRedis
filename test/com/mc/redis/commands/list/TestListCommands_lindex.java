package com.mc.redis.commands.list;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestListCommands_lindex extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectBulkReply();
    }
    
    @Test public void lindex_default() throws Exception {
        assertBulkReply(client.lindex(KEY, BEGIN));
        assertRequest("LINDEX", KEY, BEGIN_STR);
    }
    
    @Test public void lindex_encoded() throws Exception {
        assertBulkReply(client.lindex(KEY, utf16, BEGIN), utf16);
        assertRequest("LINDEX", KEY, BEGIN_STR);
    }
    
    @Test public void lindex_binary() throws Exception {
        assertBulkReplyBinary(client.lindexBytes(KEY, BEGIN));
        assertRequest("LINDEX", KEY, BEGIN_STR);
    }
}
