package com.mc.redis.livetests;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class LiveLearningExperiment extends LiveTests {
    
    @Test public void blpopReturnsNullUponTimeout() {
        Assert.assertNull(liveClient.blpop(1, "KEY1", "KEY2"));
        //note:  This passes.  BLPOP returns a bulk-reply null upon timeout
    }
}
