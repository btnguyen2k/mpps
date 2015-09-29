package com.github.btnguyen2k.mpps.handler.engine;

public class CacheItem {
    private String key;
    private long timestampCreate, ttl, timestampExpire;
    private int flags;
    private int size;
    private byte[] value;

    public CacheItem() {
    }

    public String getKey() {
        return key;
    }

    public CacheItem setKey(String key) {
        this.key = key;
        return this;
    }

    public long getTimestampCreate() {
        return timestampCreate;
    }

    public CacheItem setTimestampCreate(long timestampCreate) {
        this.timestampCreate = timestampCreate;
        return this;
    }

    public long getTtl() {
        return ttl;
    }

    public CacheItem setTtl(long ttl) {
        this.ttl = ttl;
        return this;
    }

    public long getTimestampExpire() {
        return timestampExpire;
    }

    public CacheItem setTimestampExpire(long timestampExpire) {
        this.timestampExpire = timestampExpire;
        return this;
    }

    public int getFlags() {
        return flags;
    }

    public CacheItem setFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public int getSize() {
        return size;
    }

    public CacheItem setSize(int size) {
        this.size = size;
        return this;
    }

    public byte[] getValue() {
        return value;
    }

    public CacheItem setValue(byte[] value) {
        this.value = value;
        return this;
    }
}
