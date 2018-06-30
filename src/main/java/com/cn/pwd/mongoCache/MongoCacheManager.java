package com.cn.pwd.mongoCache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MongoCacheManager extends AbstractCacheManager {

    private final Collection<MongoCacheBuilder> initialCaches;

    public Collection<MongoCacheBuilder> getInitialCaches() {
        return initialCaches;
    }

    public MongoCacheManager(Collection<MongoCacheBuilder> initialCaches) {
        this.initialCaches = initialCaches;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {

        final Collection<Cache> caches = new LinkedHashSet<>(initialCaches.size());
        for(MongoCacheBuilder builder : initialCaches){
            final MongoCache cache = builder.builder();
            caches.add(cache);
        }
        return null;
    }

    protected Cache getMissingCache(String name) {
        if(initialCaches != null && !initialCaches.isEmpty()){
            MongoTemplate mongoTemplate;
            String collection = "";
            Iterator<MongoCacheBuilder> iterator = initialCaches.iterator();
            while (iterator.hasNext()){
                MongoCacheBuilder builder = iterator.next();
                mongoTemplate = builder.getMongoTemplate();
                collection = builder.getCollectionName();
                if(mongoTemplate != null && !"".equals(collection)){
                 break;
                }
                MongoCacheBuilder cacheBuilder = MongoCacheBuilder.newInstance(collection,name,mongoTemplate);
            }
        }
        return null;
    }
}
