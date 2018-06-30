package com.cn.pwd.mongoCache;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

public class MongoCacheBuilder {

    private static final long DEFAULT_TTL = TimeUnit.DAYS.toSeconds(7);

    private  boolean flushOnBoot;

    private  String collectionName;

    private  String cacheName;

    private  MongoTemplate mongoTemplate;

    private  long ttl;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public MongoCacheBuilder(String collectionName, String cacheName, MongoTemplate mongoTemplate) {
        this.collectionName = collectionName;
        this.cacheName = cacheName;
        this.mongoTemplate = mongoTemplate;
        this.ttl = DEFAULT_TTL;
    }

    public static  MongoCacheBuilder newInstance(String collectionName, String cacheName, MongoTemplate mongoTemplate){
       return  new MongoCacheBuilder(collectionName,cacheName,mongoTemplate);
    }

    public MongoCache builder(){
        return new MongoCache(flushOnBoot,collectionName, cacheName, mongoTemplate,ttl);
    }

    public MongoCacheBuilder withFlushOnBoot(boolean flushOnBoot){
        this.flushOnBoot = flushOnBoot;
        return this;
    }

    public MongoCacheBuilder withTtl(long ttl){
        this.ttl = ttl;
        return this;
    }

}
