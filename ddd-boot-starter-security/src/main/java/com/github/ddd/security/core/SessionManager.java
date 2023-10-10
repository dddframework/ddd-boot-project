package com.github.ddd.security.core;

import com.github.ddd.common.util.JacksonUtil;
import com.github.ddd.security.config.SecurityProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.concurrent.TimeUnit;

/**
 * @author 研发中心-ranger
 */
public class SessionManager {

    private final CacheManager cacheManager;
    private final SecurityProperties securityProperties;

    protected Cache cache;

    public SessionManager(CacheManager cacheManager, SecurityProperties securityProperties) {
        this.cacheManager = cacheManager;
        this.securityProperties = securityProperties;
        this.cache = cacheManager.getCache(securityProperties.getSessionPrefix());
    }

    public void put(String key, Object o, long duration, TimeUnit timeUnit) {
        cache.put(key, JacksonUtil.toJsonStr(o));
        cache.put(key + ".expires", System.currentTimeMillis() + timeUnit.toMillis(duration));
    }

    public void put(String key, Object o) {
        cache.put(key, JacksonUtil.toJsonStr(o));
    }

    public void remove(String key) {
        cache.evict(key);
    }

    public String get(String key) {
        Cache.ValueWrapper wrapper = cache.get(key);
        if (wrapper != null) {
            Object o = wrapper.get();
            if (o != null) {
                return o.toString();
            }
        }
        return null;
    }

    public void expire(String key, long duration, TimeUnit timeUnit) {
        cache.put(key, cache.get(key));
        cache.put(key + ".expires", System.currentTimeMillis() + timeUnit.toMillis(duration));
    }
}
