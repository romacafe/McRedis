package com.mc.redis.commands;

import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestRemoteServerControlCommands extends RedisClientTest {

    @Test
    public void info() throws Exception {
        expectBulkReply();
        assertBulkReply(client.info());
        assertRequest("INFO");
    }

}
