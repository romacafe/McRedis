package com.mc.redis;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class McRedis implements RedisClient {
    
    private Socket sock;
    protected InputStream in;
    protected OutputStream out;
    private RedisProtocol protocol;
    
    public McRedis(String host, int port) throws IOException {
        protocol = new RedisUniversalProtocol();
        setupSocketAndStreams(host, port);
    }

    protected void setupSocketAndStreams(String host, int port) throws IOException {
        sock = new Socket(host, port);
        in = new BufferedInputStream(sock.getInputStream());
        out = new BufferedOutputStream(sock.getOutputStream());
    }
    
    protected long toUnixTime(long javaTime) {
        return javaTime/1000;
    }
    
    protected byte[] toBytes(int number) {
        return String.valueOf(number).getBytes(utf8);
    }
    
    protected byte[] toBytes(String value) {
        return toBytes(value, utf8);
    }
    
    protected byte[] toBytes(String value, Charset charset) {
        return value == null ? null : value.getBytes(charset);
    }
    
    protected String toString(byte[] bytes) {
        return this.toString(bytes, utf8);
    }
    
    protected String toString(byte[] bytes, Charset encoding) {
        return bytes == null ? null : new String(bytes, encoding);
    }

    protected byte[][] encodeValuesOnly(Charset charset, String[] keysAndValues) {
        byte[][] barray = new byte[keysAndValues.length][];
        for (int i=0; i<keysAndValues.length; i++) {
            if (i % 2 == 0) {
                barray[i] = toBytes(keysAndValues[i], utf8);
            } else {
                barray[i] = toBytes(keysAndValues[i], charset);
            }
        }
        return barray;
    }


    @Override
    public String info() {
        protocol.writeRequest(out, "INFO");
        return protocol.readBulkReply(in);
    }
    
    @Override
    public String auth(String password) throws RedisException {
        protocol.writeRequest(out, "AUTH", password);
        return protocol.readSingleLineReply(in);
    }
    
    @Override
    public void quit() {
        protocol.writeRequest(out, "QUIT");
    }
    
    @Override
    public void close() throws IOException {
        this.quit();
    }
    
    @Override
    public boolean exists(String key) throws RedisException {
        protocol.writeRequest(out, "EXISTS", key);
        return protocol.readBoolean(in);
    }
    
    @Override
    public int del(String... keys) throws RedisException {
        protocol.writeRequest(out, "DEL", keys);
        return protocol.readSmallInt(in);
    }

    @Override
    public String type(String key) throws RedisException {
        protocol.writeRequest(out, "TYPE", key);
        return protocol.readSingleLineReply(in);
    }

    @Override
    public String[] keys(String pattern) throws RedisException {
        protocol.writeRequest(out, "KEYS", pattern);
        return protocol.readMultiBulkReply(in);
    }

    @Override
    public String randomKey() throws RedisException {
        protocol.writeRequest(out, "RANDOMKEY");
        return protocol.readSingleLineReply(in);
    }

    @Override
    public void rename(String oldKey, String newKey) throws RedisException {
        protocol.writeRequest(out, "RENAME", oldKey, newKey);
        protocol.readSingleLineReply(in); //read it and forget it?
    }

    @Override
    public boolean renamenx(String oldKey, String newKey) throws RedisException {
        protocol.writeRequest(out, "RENAMENX", oldKey, newKey);
        return protocol.readBoolean(in);
    }

    @Override
    public long dbSize() throws RedisException {
        protocol.writeRequest(out, "DBSIZE");
        return protocol.readLong(in);
    }

    @Override
    public boolean expire(String key, int seconds) throws RedisException {
        protocol.writeRequest(out, "EXPIRE", key, String.valueOf(seconds));
        return protocol.readBoolean(in);
    }
    
    @Override
    public boolean expireat(String key, long timestamp) throws RedisException {
        protocol.writeRequest(out, "EXPIREAT", key, String.valueOf(toUnixTime(timestamp)));
        return protocol.readBoolean(in);
    }

    @Override
    public boolean persist(String key) throws RedisException {
        protocol.writeRequest(out, "PERSIST", key);
        return protocol.readBoolean(in);
    }

    @Override
    public int ttl(String key) throws RedisException {
        protocol.writeRequest(out, "TTL", key);
        return protocol.readSmallInt(in);
    }

    @Override
    public void select(int dbNumber) throws RedisException {
        protocol.writeRequest(out, "SELECT", String.valueOf(dbNumber));
        protocol.readSingleLineReply(in);
    }

    @Override
    public void move(String key, int dbNumber) throws RedisException {
        protocol.writeRequest(out, "MOVE", key, String.valueOf(dbNumber));
        protocol.readSingleLineReply(in);
    }

    @Override
    public void flushdb() throws RedisException {
        protocol.writeRequest(out, "FLUSHDB");
        protocol.readSingleLineReply(in);
    }

    @Override
    public void flushall() throws RedisException {
        protocol.writeRequest(out, "FLUSHALL");
        protocol.readSingleLineReply(in);
    }
    
    @Override
    public void set(String key, String value) {
        this.setBytes(key, toBytes(value, utf8));
    }
    
    @Override
    public void set(String key, String value, Charset charset)
            throws RedisException {
        this.setBytes(key, toBytes(value, charset));
    }
    
    @Override
    public void setBytes(String key, byte[] value) throws RedisException {
        protocol.writeRequest(out, "SET", key.getBytes(utf8), value);
        protocol.readSingleLineReply(in);
    }
    
    @Override
    public String get(String key) throws RedisException {
        return this.get(key, utf8);
    }
    
    @Override
    public String get(String key, Charset charset) throws RedisException {
        protocol.writeRequest(out, "GET", key);
        return protocol.readBulkReply(in, charset);
    }
    
    @Override
    public byte[] getBytes(String key) throws RedisException {
        protocol.writeRequest(out, "GET", key);
        return protocol.readBulkReplyBinary(in);
    }
    
    @Override
    public String getset(String key, String value) throws RedisException {
        return this.getset(key, value, utf8);
    }
    
    @Override
    public String getset(String key, String value, Charset charset)
            throws RedisException {
        protocol.writeRequest(out, "GETSET", key.getBytes(utf8), toBytes(value, charset));
        return protocol.readBulkReply(in, charset);
    }
    
    @Override
    public byte[] getsetBytes(String key, byte[] value) throws RedisException {
        protocol.writeRequest(out, "GETSET", key.getBytes(utf8), value);
        return protocol.readBulkReplyBinary(in);
    }
    
    @Override
    public String[] mget(String... keys) throws RedisException {
        return this.mget(utf8, keys);
    }
    
    @Override
    public String[] mget(Charset charset, String... keys) throws RedisException {
        protocol.writeRequest(out, "MGET", keys);
        return protocol.readMultiBulkReply(in, charset);
    }
    
    @Override
    public byte[][] mgetBytes(String... keys) throws RedisException {
        protocol.writeRequest(out, "MGET", keys);
        return protocol.readMultiBulkReplyBinary(in);
    }
    
    @Override
    public boolean setnx(String key, String value) throws RedisException {
        return this.setnxBytes(key, toBytes(value, utf8));
    }
    
    @Override
    public boolean setnx(String key, String value, Charset charset) throws RedisException {
        return this.setnxBytes(key, toBytes(value, charset));
    }
    
    @Override
    public boolean setnxBytes(String key, byte[] value) throws RedisException {
        protocol.writeRequest(out, "SETNX", key.getBytes(utf8), value);
        return protocol.readBoolean(in);
    }
    
    @Override
    public void setex(String key, String value, int ttl) throws RedisException {
        this.setexBytes(key, toBytes(value, utf8), ttl);
    }
    
    @Override
    public void setex(Charset charset, String key, String value, int ttl) throws RedisException {
        this.setexBytes(key, toBytes(value, charset), ttl);
    }
    
    @Override
    public void setexBytes(String key, byte[] value, int ttl) throws RedisException {
        protocol.writeRequest(out, "SETEX", key.getBytes(), value, toBytes(ttl));
        protocol.readSingleLineReply(in);
    }
    
    @Override
    public void mset(String... keysAndValues) throws RedisException {
        this.mset(utf8, keysAndValues);
    }
    
    @Override
    public void mset(Charset charset, String... keysAndValues) throws RedisException {
        byte[][] keysAndValuesEncoded = encodeValuesOnly(charset, keysAndValues);
        protocol.writeRequest(out, "MSET", keysAndValuesEncoded);
        protocol.readSingleLineReply(in);
    }
    
    @Override
    public boolean msetnx(String... keysAndValues) throws RedisException {
        return this.msetnx(utf8, keysAndValues);
    }
    
    @Override
    public boolean msetnx(Charset encoding, String... keysAndValues) throws RedisException {
        byte[][] keysAndValuesEncoded = encodeValuesOnly(encoding, keysAndValues);
        protocol.writeRequest(out, "MSETNX", keysAndValuesEncoded);
        return protocol.readBoolean(in);
    }
    
    @Override
    public long incr(String key) throws RedisException {
        protocol.writeRequest(out, "INCR", key);
        return protocol.readLong(in);
    }
    
    @Override
    public long incrby(String key, int increment) throws RedisException {
        protocol.writeRequest(out, "INCRBY", key, String.valueOf(increment));
        return protocol.readLong(in);
    }
    
    @Override
    public long decr(String key) throws RedisException {
        protocol.writeRequest(out, "DECR", key);
        return protocol.readLong(in);
    }
    
    @Override
    public long decrby(String key, int decrement) throws RedisException {
        protocol.writeRequest(out, "DECRBY", key, String.valueOf(decrement));
        return protocol.readLong(in);
    }
    
    @Override
    public int append(String key, String value) throws RedisException {
        return this.appendBytes(key, toBytes(value, utf8));
    }
    
    @Override
    public int append(String key, String value, Charset encoding) throws RedisException {
        return this.appendBytes(key, toBytes(value, encoding));
    }
    
    @Override
    public int appendBytes(String key, byte[] value) throws RedisException {
        protocol.writeRequest(out, "APPEND", key.getBytes(utf8), value);
        return protocol.readSmallInt(in);
    }
    
    @Override
    public String substr(String key, int begin, int end) throws RedisException {
        return substr(key, begin, end, utf8);
    }
    
    @Override
    public String substr(String key, int begin, int end, Charset encoding) throws RedisException {
        protocol.writeRequest(out, "SUBSTR", key, String.valueOf(begin), String.valueOf(end));
        return protocol.readBulkReply(in, encoding);
    }
    
    @Override
    public byte[] substrBytes(String key, int begin, int end) throws RedisException {
        protocol.writeRequest(out, "SUBSTR", key, String.valueOf(begin), String.valueOf(end));
        return protocol.readBulkReplyBinary(in);
    }
    
    @Override
    public int lpush(String key, String value) throws RedisException {
        return this.lpushBytes(key, toBytes(value, utf8));
    }
    
    @Override
    public int lpush(String key, String value, Charset encoding) throws RedisException {
        return this.lpushBytes(key, toBytes(value, encoding));
    }
    
    @Override
    public int lpushBytes(String key, byte[] value) throws RedisException {
        protocol.writeRequest(out, "LPUSH", key.getBytes(utf8), value);
        return protocol.readSmallInt(in);
    }
    
    @Override
    public int rpush(String key, String value) throws RedisException {
        return this.rpushBytes(key, toBytes(value, utf8));
    }
    
    @Override
    public int rpush(String key, String value, Charset encoding) throws RedisException {
        return this.rpushBytes(key, toBytes(value, encoding));
    }
    
    @Override
    public int rpushBytes(String key, byte[] value) throws RedisException {
        protocol.writeRequest(out, "RPUSH", key.getBytes(utf8), value);
        return protocol.readSmallInt(in);
    }
    
    @Override
    public int llen(String key) throws RedisException {
        protocol.writeRequest(out, "LLEN", key);
        return protocol.readSmallInt(in);
    }
    
    @Override
    public String[] lrange(String key, int begin, int end) {
        return this.lrange(key, utf8, begin, end);
    }
    
    @Override
    public String[] lrange(String key, Charset encoding, int begin, int end) {
        protocol.writeRequest(out, "LRANGE", key.getBytes(utf8), toBytes(begin), toBytes(end));
        return protocol.readMultiBulkReply(in, encoding);
    }
    
    @Override
    public byte[][] lrangeBytes(String key, int begin, int end) {
        protocol.writeRequest(out, "LRANGE", key.getBytes(utf8), toBytes(begin), toBytes(end));
        return protocol.readMultiBulkReplyBinary(in);
    }
    
    @Override
    public void ltrim(String key, int begin, int end) throws RedisException {
        protocol.writeRequest(out, "LTRIM", key.getBytes(utf8), toBytes(begin), toBytes(end));
        protocol.readSingleLineReply(in);
    }
    
    @Override
    public String lindex(String key, int index) throws RedisException {
        return this.lindex(key, utf8, index);
    }
    
    @Override
    public String lindex(String key, Charset encoding, int index) {
        protocol.writeRequest(out, "LINDEX", toBytes(key, utf8), toBytes(index));
        return protocol.readBulkReply(in, encoding);
    }
    
    @Override
    public byte[] lindexBytes(String key, int index) throws RedisException {
        protocol.writeRequest(out, "LINDEX", toBytes(key, utf8), toBytes(index));
        return protocol.readBulkReplyBinary(in);
    }
    
    @Override
    public void lset(String key, int index, String value) throws RedisException {
        this.lsetBytes(key, index, toBytes(value));
    }
    
    @Override
    public void lset(String key, int index, String value, Charset encoding) throws RedisException {
        this.lsetBytes(key, index, toBytes(value, encoding));
    }
    
    @Override
    public void lsetBytes(String key, int index, byte[] value) throws RedisException {
        protocol.writeRequest(out, "LSET", toBytes(key), toBytes(index), value);
        protocol.readSingleLineReply(in);
    }
    
    @Override
    public int lrem(String key, int count, String value) throws RedisException {
        return this.lremBytes(key, count, toBytes(value));
    }
    
    @Override
    public int lrem(String key, int count, String value, Charset encoding) throws RedisException {
        return this.lremBytes(key, count, toBytes(value, encoding));
    }
    
    @Override
    public int lremBytes(String key, int count, byte[] value) throws RedisException {
        protocol.writeRequest(out, "LREM", toBytes(key), toBytes(count), value);
        return protocol.readSmallInt(in);
    }
    
    @Override
    public String lpop(String key) throws RedisException {
        return toString(this.lpopBytes(key));
    }
    
    @Override
    public String lpop(String key, Charset encoding) throws RedisException {
        return toString(this.lpopBytes(key), encoding);
    }
    
    @Override
    public byte[] lpopBytes(String key) throws RedisException {
        protocol.writeRequest(out, "LPOP", key);
        return protocol.readBulkReplyBinary(in);
    }
    
    @Override
    public String rpop(String key) throws RedisException {
        return toString(this.rpopBytes(key));
    }
    
    @Override
    public String rpop(String key, Charset encoding) throws RedisException {
        return toString(this.rpopBytes(key), encoding);
    }
    
    @Override
    public byte[] rpopBytes(String key) throws RedisException {
        protocol.writeRequest(out, "RPOP", key);
        return protocol.readBulkReplyBinary(in);
    }
    
    @Override
    public String[] blpop(int timeoutSeconds, String... keys) throws RedisException {
        return this.blpop(timeoutSeconds, utf8, keys);
    }
    
    @Override
    public String[] blpop(int timeoutSeconds, Charset encoding, String... keys) throws RedisException {
        byte[][] byteArrays = blpopBytes(timeoutSeconds, keys);
        if (byteArrays == null) return null;
        String[] strings = new String[2];
        strings[0] = new String(byteArrays[0], utf8);
        strings[1] = new String(byteArrays[1], encoding);
        return strings;
    }
    
    @Override
    public byte[][] blpopBytes(int timeoutSeconds, String... keys) throws RedisException {
        sendCommand("BLPOP", timeoutSeconds, keys);
        byte[][] bytes = protocol.readMultiBulkReplyBinary(in);
        return bytes;
    }
    
    @Override
    public String[] brpop(int timeoutSeconds, String... keys) throws RedisException {
        return this.brpop(timeoutSeconds, utf8, keys);
    }
    
    @Override
    public String[] brpop(int timeoutSeconds, Charset encoding, String... keys) throws RedisException {
        byte[][] byteArrays = brpopBytes(timeoutSeconds, keys);
        if (byteArrays == null) return null;
        String[] strings = new String[2];
        strings[0] = new String(byteArrays[0], utf8);
        strings[1] = new String(byteArrays[1], encoding);
        return strings;
    }
    
    @Override
    public byte[][] brpopBytes(int timeoutSeconds, String... keys) throws RedisException {
        sendCommand("BRPOP", timeoutSeconds, keys);
        return protocol.readMultiBulkReplyBinary(in);
    }
    
    @Override
    public String rpoplpush(String sourceList, String destList) throws RedisException {
        return rpoplpush(sourceList, destList, utf8);
    }
    
    @Override
    public String rpoplpush(String sourceList, String destList, Charset encoding) throws RedisException {
        protocol.writeRequest(out, "RPOPLPUSH", sourceList, destList);
        return protocol.readBulkReply(in, encoding);
    }
    
    @Override
    public byte[] rpoplpushBytes(String sourceList, String destList) throws RedisException {
        protocol.writeRequest(out, "RPOPLPUSH", sourceList, destList);
        return protocol.readBulkReplyBinary(in);
    }

    private void sendCommand(String command, int timeoutSeconds, String... keys) {
        String[] args = new String[keys.length+1];
        System.arraycopy(keys, 0, args, 0, keys.length);
        args[keys.length] = String.valueOf(timeoutSeconds);
        protocol.writeRequest(out, command, args);
    }
}
