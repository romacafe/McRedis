package com.mc.redis.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.mc.redis.McRedis;

public class MockClient extends McRedis {
    public MockClient() throws IOException {
        super("host & port not used", 9999);
    }
    
    @Override
    protected void setupSocketAndStreams(String host, int port)
            throws IOException {
        this.out = new ByteArrayOutputStream();
        this.in = new ByteArrayInputStream(new byte[0]);
    }
    
    public void programForResponse(ByteArrayInputStream in) throws IOException {
        this.out = new ByteArrayOutputStream();
        this.in.close();
        this.in = in;
    }
    
    public ByteArrayOutputStream getOutputStream() {
        return (ByteArrayOutputStream)this.out;
    }
}
