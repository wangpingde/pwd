package com.cn.pwd.mongoCache;

import com.cn.pwd.domain.CacheDocument;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MongoCache implements Cache {

    private static final Logger log = LoggerFactory.getLogger(MongoCache.class);

    private static final long DEFAULT_TTL = TimeUnit.DAYS.toSeconds(30);

    private static final String INDEX_KEY_NAME = "creationDate";

    private static final String INDEX_NAME = "_expire";

    private final boolean flushOnBoot;

    private final String collectionName;

    private final String cacheName;

    private final MongoTemplate mongoTemplate;

    private final long ttl;

    private final Lock lock = new ReentrantLock();

    public MongoCache(boolean flushOnBoot, String collectionName, String cacheName, MongoTemplate mongoTemplate) {
        this(flushOnBoot, collectionName, cacheName, mongoTemplate, DEFAULT_TTL);
    }

    public MongoCache(String collectionName, String cacheName, MongoTemplate template, long ttl) {
        this(false, collectionName, cacheName, template, ttl);
    }

    public MongoCache(boolean flushOnBoot, String collectionName, String cacheName, MongoTemplate mongoTemplate, long ttl) {
        this.flushOnBoot = flushOnBoot;
        this.collectionName = collectionName;
        this.cacheName = cacheName;
        this.mongoTemplate = mongoTemplate;
        this.ttl = ttl;
        initialize();
    }


    private void creationCollection() {
        mongoTemplate.getCollection(collectionName);
    }

    @Override
    public String getName() {
        return cacheName;
    }

    public boolean isFlushOnBoot() {
        return flushOnBoot;
    }

    @Override
    public Object getNativeCache() {
        return mongoTemplate;
    }

    public long getTtl() {
        return ttl;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object o = getFromCache(key);
        if (o != null) {
            return new SimpleValueWrapper(o);
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> aClass) {
        Object o = getFromCache(key);
        if (o == null) {
            return null;
        }
        return aClass.cast(o);
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        Assert.isTrue(key instanceof String, " key must be string");
        Assert.isNull(callable, "callable must be not empty");
        Object o = getFromCache(getCacheKey(key));
        if (o != null) {
            return (T) o;
        }
        lock.lock();
        try {
            o = getFromCache(getCacheKey(key));
            if (o != null) {
                return (T) o;
            }
            T value;
            try {
                value = callable.call();
            } catch (Throwable e) {
                throw new ValueRetrievalException(key, callable, e);
            }
            ValueWrapper newValue = putIfAbsent(key, value);
            if(newValue!=null){
                return (T) newValue.get();
            }else{
                return value;
            }
        } catch (Exception e) {

        } finally {
             lock.unlock();
        }
        return null;
    }

    @Override
    public void put(Object key, Object o) {
        Assert.isTrue(key instanceof String, " key must be string");
        String id = getCacheKey(key);
        String result = "";
        if (o != null) {
            Assert.isTrue(o instanceof Serializable, " key must be Serializable");
            result = serialize(o);
        }
        final CacheDocument cache = new CacheDocument(id, result);
        mongoTemplate.save(cache, collectionName);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object o) {
        Assert.isTrue(key instanceof String, " key must be string");
        String id = getCacheKey(key);
        String result = "";
        if (o != null) {
            Assert.isTrue(o instanceof Serializable, " key must be Serializable");
            result = serialize(o);
        }
        final CacheDocument cache = new CacheDocument(id, result);
        mongoTemplate.insert(cache, collectionName);
        return null;
    }

    @Override
    public void evict(Object key) {

        Assert.isTrue(key instanceof String, " key must be string");

        final String id = getCacheKey(key);

        CriteriaDefinition criteria = Criteria.where("_id").is(id);

        final Query query = new Query(criteria);

        mongoTemplate.remove(query, collectionName);

    }

    @Override
    public void clear() {
        mongoTemplate.remove(new Query(), CacheDocument.class, collectionName);
    }

    private Object deserialize(String value) {

        Base64.Decoder decoder = Base64.getDecoder();
        final byte[] data = decoder.decode(value);
        try {
            final ByteArrayInputStream buffer = new ByteArrayInputStream(data);
            final ObjectInputStream output = new ObjectInputStream(buffer);
            return output.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getFromCache(Object key) {
        Assert.isTrue(key instanceof String, " key must be string");

        final String id = getCacheKey(key);
        final CacheDocument cache = mongoTemplate.findById(id, CacheDocument.class, collectionName);

        if (cache != null) {
            String element = cache.getElement();
            if (!StringUtils.isEmpty(element)) {
                return deserialize(element);
            }
        }
        return null;
    }

    private void initialize() {
        creationCollection();
        if (isFlushOnBoot()) {
            clear();
        }
        Index index = this.createExpireIndex();
        this.updateExpireIndex(index);
    }

    private String serialize(Object data) {
        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final ObjectOutputStream output = new ObjectOutputStream(buffer);
            output.writeObject(data);
            byte[] value = buffer.toByteArray();
            final Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Index createExpireIndex() {
        final Index index = new Index();
        index.named(INDEX_NAME);
        index.on(INDEX_KEY_NAME, Sort.Direction.ASC);
        index.expire(ttl);
        return index;
    }

    private void updateExpireIndex(Index newExpireIndex) {
        final IndexOperations indexOperations = mongoTemplate.indexOps(collectionName);
        final DBCollection collection = mongoTemplate.getCollection(collectionName);
        final List<DBObject> indexs = collection.getIndexInfo();

        final Optional<DBObject> expireOptional = indexs.stream()
                .filter(index -> INDEX_NAME.equals(index.get("name")))
                .findFirst();

        if (expireOptional.isPresent()) {
            final DBObject expire = expireOptional.get();
            final long ttl = (long) expire.get("expireAfterSeconds");
            if (ttl != this.ttl) {
                indexOperations.dropIndex(INDEX_NAME);
            }
        }
        indexOperations.ensureIndex(newExpireIndex);
    }

    private String getCacheKey(Object key) {
        Assert.isTrue(key instanceof Serializable, " key must be Serializable");
        Assert.isTrue(key != null && !"".equals(key.toString()), " key must be not empty");
        String cacheKey = this.cacheName + ":" + key.toString().replaceAll("\\s+", "");
        return cacheKey;
    }

}
