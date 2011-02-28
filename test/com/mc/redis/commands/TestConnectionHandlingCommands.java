package com.mc.redis.commands;

import org.junit.Assert;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestConnectionHandlingCommands extends RedisClientTest {

    @Test
    public void auth() throws Exception {
    	expectSingleLineReply();
    	assertSingleLineReply(client.auth("PASSWORD"));
    	assertRequest("AUTH", "PASSWORD");
    	
    	expectExceptionReply();
    	try {
    	    client.auth("PASSWORD");
    	    Assert.fail("Auth method should have thrown an exception");
    	} catch (Exception e) {
    	    assertExceptionReply(e);
    	}
    }

    @Test
    public void quit() throws Exception {
        expectNoReply();
        client.quit();
        assertRequest("QUIT");
    }

    @Test
    public void close() throws Exception {
        expectNoReply();
        client.close();
        assertRequest("QUIT");
    }

}
