package com.mc.redis.commands.hash;

import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestHashCommands_hdel extends RedisClientTest {
   
    @Before
    public void reset() throws Exception {
        expectIntegerReply();
    }
    
    @Test
    public void hdel() throws Exception {
    }
}
