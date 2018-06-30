package com.cn.pwd.mongoCache.autoconfig;


import java.util.concurrent.TimeUnit;

public class MongoCacheProperties {

    private  boolean flushOnBoot;

    private  String collectionName;

    private  String cacheName;

    private  long ttl = TimeUnit.DAYS.toSeconds(7);

    public boolean isFlushOnBoot() {
        return flushOnBoot;
    }

    public void setFlushOnBoot(boolean flushOnBoot) {
        this.flushOnBoot = flushOnBoot;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
