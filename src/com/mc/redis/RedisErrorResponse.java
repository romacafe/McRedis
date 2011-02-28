package com.mc.redis;

public class RedisErrorResponse extends RedisException {
    public RedisErrorResponse(String message) {
        super(message);
    }
}
