package com.cn.pwd.mongoCache;


import com.cn.pwd.mongoCache.autoconfig.MongoCacheProperties;
import com.cn.pwd.mongoCache.autoconfig.MongoCachePropertiesList;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass(MongoTemplate.class)
public class MongoCacheAutoConfiguration {

   /* @Autowired
    private MongoTemplate mongoTemplate;*/

    @Autowired
    private MongoCachePropertiesList propertiesList;

    @Bean
    @ConditionalOnProperty("spring.cache.mongo.caches[0].cacheName")
    public CacheManager mongoCacheManager(MongoClient client){
        String database = propertiesList.getDatabase();
        MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(client,database));
        return new MongoCacheManager(mongoCacheBuilders(mongoTemplate));

    }

    private List<MongoCacheBuilder> mongoCacheBuilders(MongoTemplate mongoTemplate){

        List<MongoCacheBuilder> builders = new ArrayList<>();

        if(propertiesList.getCaches()!=null){

            for(MongoCacheProperties  mongoCacheProperties : propertiesList.getCaches()){

                builders.add(MongoCacheBuilder.
                        newInstance(mongoCacheProperties.getCollectionName(),
                                     mongoCacheProperties.getCacheName()
                                      ,mongoTemplate)
                        .withTtl(mongoCacheProperties.getTtl())
                        .withFlushOnBoot(mongoCacheProperties.isFlushOnBoot())
                );

            }

        }

        return builders;
    }



}
