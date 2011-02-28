package com.mc.redis.commands;

import junit.framework.Assert;

import org.junit.Test;

import com.mc.redis.RedisClientTest;

public class TestCommonCommands extends RedisClientTest {
    @Test public void exists_true() throws Exception {
        expectBooleanReply(true);
        Assert.assertTrue(client.exists(KEY));
        assertRequest("EXISTS", KEY);
    }
    
    @Test public void exists_false() throws Exception {
        expectBooleanReply(false);
        Assert.assertFalse(client.exists(KEY));
        assertRequest("EXISTS", KEY);
    }
    
    @Test public void del() throws Exception {
        expectIntegerReply();
        assertIntegerReply(client.del(KEY, "KEY2"));
        assertRequest("DEL", KEY, "KEY2");
    }
    
    @Test public void type() throws Exception {
        expectSingleLineReply();
        assertSingleLineReply(client.type(KEY));
        assertRequest("TYPE", KEY);
    }
    
    @Test public void keys() throws Exception {
        expectMultiBulkReply();
        assertMultiBulkReply(client.keys("PATTERN*"));
        assertRequest("KEYS", "PATTERN*");
    }
    
    @Test public void randomKey() throws Exception {
        expectSingleLineReply();
        assertSingleLineReply(client.randomKey());
        assertRequest("RANDOMKEY");
    }
    
    @Test public void rename() throws Exception {
        expectSingleLineReply();
        client.rename(KEY, "KEY2");
        assertVoidResponse();
        assertRequest("RENAME", "KEY", "KEY2");
    }
    
    @Test public void renamenx() throws Exception {
        expectBooleanReply(true);
        Assert.assertTrue(client.renamenx(KEY, "KEY2"));
        assertRequest("RENAMENX", KEY, "KEY2");
    }
    
    @Test public void dbSize() throws Exception {
        expectLongReply(40L);
        Assert.assertEquals(40L, client.dbSize());
        assertRequest("DBSIZE");
    }
    
    @Test public void expire() throws Exception {
        expectBooleanReply(true);
        Assert.assertTrue(client.expire(KEY, 11));
        assertRequest("EXPIRE", KEY, "11");
    }
    
    @Test public void expireat() throws Exception {
        expectBooleanReply(false);
        Assert.assertFalse(client.expireat(KEY, 123456789L));
        assertRequest("EXPIREAT", KEY, String.valueOf(123456789L / 1000));
    }
    
    @Test public void persist() throws Exception {
        expectBooleanReply(true);
        Assert.assertTrue(client.persist(KEY));
        assertRequest("PERSIST", KEY);
    }
    
    @Test public void ttl() throws Exception {
        expectIntegerReply();
        assertIntegerReply(client.ttl(KEY));
        assertRequest("TTL", KEY);
    }
    
    @Test public void select() throws Exception {
        expectSingleLineReply();
        client.select(4);
        assertVoidResponse();
        assertRequest("SELECT", "4");
    }
    
    @Test public void move() throws Exception {
        expectSingleLineReply();
        client.move(KEY, 6);
        assertVoidResponse();
        assertRequest("MOVE", KEY, "6");
    }
    
    @Test public void flushdb() throws Exception {
        expectSingleLineReply();
        client.flushdb();
        assertVoidResponse();
        assertRequest("FLUSHDB");
    }
    
    @Test public void flushall() throws Exception {
        expectSingleLineReply();
        client.flushall();
        assertVoidResponse();
        assertRequest("FLUSHALL");
    }
}
