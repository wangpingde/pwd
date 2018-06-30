package com.cn.pwd.mongoCache.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.cache.mongo")
public class MongoCachePropertiesList {

    private List<MongoCacheProperties> caches;

    public List<MongoCacheProperties> getCaches() {
        return caches;
    }

    public void setCaches(List<MongoCacheProperties> caches) {
        this.caches = caches;
    }
}
