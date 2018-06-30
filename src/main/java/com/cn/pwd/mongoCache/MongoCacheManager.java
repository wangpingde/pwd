package com.cn.pwd.mongoCache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;

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
        return caches;
    }

    protected Cache getMissingCache(String name) {
        if(initialCaches != null && !initialCaches.isEmpty()){
            MongoTemplate mongoTemplate = null;
            String collection = "";
            Iterator<MongoCacheBuilder> iterator = initialCaches.iterator();
            boolean isCacheConfig = false;
            MongoCacheBuilder builder;
            while (iterator.hasNext()){
                 builder = iterator.next();
                mongoTemplate = builder.getMongoTemplate();
                collection = builder.getCollectionName();
                if(mongoTemplate != null && !StringUtils.isEmpty(collection) && !StringUtils.isEmpty(builder.getCacheName())
                        && name.equalsIgnoreCase(builder.getCacheName())){
                    isCacheConfig = true;
                    break;
                }
            }
              builder = null;
            if(isCacheConfig){
                builder = MongoCacheBuilder.newInstance(collection,name,mongoTemplate);
            }else{
                builder = MongoCacheBuilder.newInstance("default_caches",name,mongoTemplate);
            }
            this.initialCaches.add(builder);
            return builder.builder();
        }
        return null;
    }
}
