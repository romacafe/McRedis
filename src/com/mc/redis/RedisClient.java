package com.mc.redis;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;

public interface RedisClient extends Closeable {
    
    public static final Charset utf8 = Charset.forName("UTF-8");
    public static final Charset utf16 = Charset.forName("UTF-16");
    public static final int DEFAULT_PORT = 6379;
    
    //Connection handling methods
    public String auth(String password) throws RedisException;
    public void quit() throws RedisException;
    @Override public void close() throws IOException;
    
    //Common commands
    public boolean exists(String key) throws RedisException;
    public int del(String... keys) throws RedisException;
    public String type (String key) throws RedisException; //TODO: Change to Enum
    public String[] keys (String pattern) throws RedisException;
    public String randomKey() throws RedisException;
    public void rename (String oldKey, String newKey) throws RedisException;
    public boolean renamenx (String oldKey, String newKey) throws RedisException;
    public long dbSize() throws RedisException;
    public boolean expire(String key, int seconds) throws RedisException;
    public boolean expireat(String key, long timestamp) throws RedisException;
    public boolean persist(String key) throws RedisException;
    public int ttl(String key) throws RedisException;
    public void select(int dbNumber) throws RedisException;
    public void move(String key, int dbNumber) throws RedisException;
    public void flushdb() throws RedisException;
    public void flushall() throws RedisException;
    
    //String commands
    public void set(String key, String value) throws RedisException;
    public void set(String key, String value, Charset encoding) throws RedisException;
    public void setBytes(String key, byte[] value) throws RedisException;
    public String get(String key) throws RedisException;
    public String get(String key, Charset encoding) throws RedisException;
    public byte[] getBytes(String key) throws RedisException;
    public String getset(String key, String value) throws RedisException;
    public String getset(String key, String value, Charset encoding) throws RedisException;
    public byte[] getsetBytes(String key, byte[] value) throws RedisException;
    public String[] mget(String... keys) throws RedisException;
    public String[] mget(Charset encoding, String... keys) throws RedisException;
    public byte[][] mgetBytes(String... keys) throws RedisException;
    public boolean setnx(String key, String value) throws RedisException;
    public boolean setnx(String key, String value, Charset encoding) throws RedisException;
    public boolean setnxBytes(String key, byte[] value) throws RedisException;
    public void setex(String key, String value, int ttl) throws RedisException;
    public void setex(Charset encoding, String key, String value, int ttl) throws RedisException;
    public void setexBytes(String key, byte[] value, int ttl) throws RedisException;
    public void mset(String... keysAndValues) throws RedisException;
    public void mset(Charset encoding, String... keysAndValues) throws RedisException;
    public boolean msetnx(String... keysAndValues) throws RedisException;
    public boolean msetnx(Charset encoding, String... keysAndValues) throws RedisException;
    public long incr(String key) throws RedisException;
    public long incrby(String key, int increment) throws RedisException;
    public long decr(String key) throws RedisException;
    public long decrby(String key, int decrement) throws RedisException;
    public int append(String key, String value) throws RedisException;
    public int append(String key, String value, Charset encoding) throws RedisException;
    public int appendBytes(String key, byte[] value) throws RedisException;
    public String substr(String key, int begin, int end) throws RedisException;
    public String substr(String key, int begin, int end, Charset encoding) throws RedisException;
    public byte[] substrBytes(String key, int begin, int end) throws RedisException;
    
    //List commands
    public int lpush(String key, String value) throws RedisException;
    public int lpush(String key, String value, Charset encoding) throws RedisException;
    public int lpushBytes(String key, byte[] value) throws RedisException;
    public int rpush(String key, String value) throws RedisException;
    public int rpush(String key, String value, Charset encoding) throws RedisException;
    public int rpushBytes(String key, byte[] value) throws RedisException;
    public int llen(String key) throws RedisException;
    public String[] lrange(String key, int begin, int end) throws RedisException;
    public String[] lrange(String key, Charset encoding, int begin, int end) throws RedisException;
    public byte[][] lrangeBytes(String key, int begin, int end) throws RedisException;
    public void ltrim(String key, int begin, int end) throws RedisException;
    public String lindex(String key, int index) throws RedisException;
    public String lindex(String key, Charset encoding, int index);
    public byte[] lindexBytes(String key, int index) throws RedisException;
    public void lset(String key, int index, String value) throws RedisException;
    public void lset(String key, int index, String value, Charset encoding) throws RedisException;
    public void lsetBytes(String key, int index, byte[] value) throws RedisException;
    public int lrem(String key, int count, String value) throws RedisException;
    public int lrem(String key, int count, String value, Charset encoding) throws RedisException;
    public int lremBytes(String key, int count, byte[] value) throws RedisException;
    public String lpop(String key) throws RedisException;
    public String lpop(String key, Charset encoding) throws RedisException;
    public byte[] lpopBytes(String key) throws RedisException;
    public String rpop(String key) throws RedisException;
    public String rpop(String key, Charset encoding) throws RedisException;
    public byte[] rpopBytes(String key) throws RedisException;
    public String[] blpop(int timeoutSeconds, String... keys) throws RedisException;
    public String[] blpop(int timeoutSeconds, Charset encoding, String... keys) throws RedisException;
    public byte[][] blpopBytes(int timeoutSeconds, String... keys) throws RedisException;
    public String[] brpop(int timeoutSeconds, String... keys) throws RedisException;
    public String[] brpop(int timeoutSeconds, Charset encoding, String... keys) throws RedisException;
    public byte[][] brpopBytes(int timeoutSeconds, String... keys) throws RedisException;
    public String rpoplpush(String sourceList, String destList) throws RedisException;
    public String rpoplpush(String sourceList, String destList, Charset encoding) throws RedisException;
    public byte[] rpoplpushBytes(String sourceList, String destList) throws RedisException;
    
    //Set commands
    
    //Sorted set commands
    
    //Hash comamnds
    
    //Sorting
    
    //Transactions
    
    //Persistance control commands
    
    
    //Remote server control commands
    public String info();
}
