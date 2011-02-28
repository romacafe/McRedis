package com.mc.redis.livetests;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.mc.redis.McRedis;
import com.mc.redis.RedisClient;


/**
 * Tests that expect a live REDIS client running at localhost:6379.
 * <P>
 * These tests may take longer to run, obviously have a dependency
 * on an external resource, and as such aren't considered a part
 * of the main test suite.  This client library is very well tested
 * without having to rely on a live connection.  We're not testing
 * Redis itself, nor are we testing the connection libraries.
 * 
 * @author mcase
 */
public abstract class LiveTests {
    
    static RedisClient liveClient;
    
    @BeforeClass public static void setupLiveClient() throws Exception {
        liveClient = new McRedis("localhost", RedisClient.DEFAULT_PORT);
    }
    
    @AfterClass public static void closeClient() throws Exception {
        liveClient.quit();
    }
}
