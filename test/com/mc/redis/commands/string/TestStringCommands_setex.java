package com.mc.redis.commands.string;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestStringCommands_setex extends RedisClientTest {
    
    @Before public void reset() throws Exception {
        expectSingleLineReply();
    }
    
    @Test public void setex_default() throws Exception {
        client.setex(KEY, "VALUE", 20);
        assertVoidResponse();
        assertRequest("SETEX", KEY.getBytes(utf8), "VALUE".getBytes(utf8), String.valueOf("20").getBytes(utf8));
    }
    
    @Test public void setex_encoded() throws Exception {
        client.setex(utf16, KEY, "VALUE", 20);
        assertVoidResponse();
        assertRequest("SETEX", KEY.getBytes(utf8), "VALUE".getBytes(utf16), String.valueOf("20").getBytes(utf8));
    }
    
    @Test public void setex_bytes() throws Exception {
        client.setexBytes(KEY, "VALUE".getBytes(utf16), 20);
        assertVoidResponse();
        assertRequest("SETEX", KEY.getBytes(utf8), "VALUE".getBytes(utf16), String.valueOf("20").getBytes(utf8));
    }
    
    @Test public void setex_exception() throws Exception {
        try {
            expectExceptionReply();
            client.setex(KEY, "VALUE", 23);
            Assert.fail();
        } catch (Exception e) {
            assertExceptionReply(e);
        }
    }
    
    @Test public void setex_null() throws Exception {
        client.setex(KEY, null, 11);
        assertRequest("SETEX", KEY.getBytes(utf8), null, String.valueOf(11).getBytes(utf8));
        
        reset();
        client.setex(utf16, KEY, null, 11);
        assertRequest("SETEX", KEY.getBytes(utf8), null, String.valueOf(11).getBytes(utf8));

        reset();
        client.setexBytes(KEY, null, 11);
        assertRequest("SETEX", KEY.getBytes(utf8), null, String.valueOf(11).getBytes(utf8));
    }
}
