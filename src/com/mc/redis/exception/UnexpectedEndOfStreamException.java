package com.mc.redis.exception;

import com.mc.redis.RedisException;

public class UnexpectedEndOfStreamException extends RedisException {

    public UnexpectedEndOfStreamException() {
        super("Reached the end of the InputStream unexpectedly");
    }

}
