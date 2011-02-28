package com.mc.redis.commands.string;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_increment_decrement extends RedisClientTest {
    @Before public void reset() throws Exception {
        expectIntegerReply();
    }
    
    @Test public void incr() throws Exception {
        assertIntegerReply(client.incr(KEY));
        assertRequest("INCR", KEY);
    }
    
    @Test public void incrby() throws Exception {
        assertIntegerReply(client.incrby(KEY, 11));
        assertRequest("INCRBY", KEY, "11");
    }
    
    @Test public void decr() throws Exception {
        assertIntegerReply(client.decr(KEY));
        assertRequest("DECR", KEY);
    }
    
    @Test public void decrby() throws Exception {
        assertIntegerReply(client.decrby(KEY, 93));
        assertRequest("DECRBY", KEY, "93");
    }
}
