package com.mc.redis;

/**
 * A blanket catch-all runtime exception for McRedis
 * @author mcase
 */
public class RedisException extends RuntimeException {
    public RedisException(String message) {
        super(message);
    }
    public RedisException(String message, Exception root) {
        super(message, root);
    }
    public RedisException(Exception root) {
        super(root.getMessage(), root);
    }
}
