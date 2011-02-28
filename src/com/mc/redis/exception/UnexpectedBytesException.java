package com.mc.redis.exception;

import com.mc.redis.RedisException;

public class UnexpectedBytesException extends RedisException {
    public UnexpectedBytesException(String message) {
        super(message);
    }
}
