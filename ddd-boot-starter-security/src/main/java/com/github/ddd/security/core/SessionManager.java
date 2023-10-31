package com.github.ddd.security.core;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.pojo.UserDetail;
import com.github.ddd.common.util.JacksonUtil;
import com.github.ddd.security.spring.boot.autoconfigure.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author ranger
 */
@Slf4j
public class SessionManager {

    private final SecurityProperties securityProperties;
    protected Cache sessionCache;

    private static final String DEFAULT_CLIENT = "default";
    private static final String SEPARATOR = "::";


    public SessionManager(CacheManager cacheManager, SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.sessionCache = cacheManager.getCache(securityProperties.getSessionPrefix());
        assert this.sessionCache != null;
        log.info("ddd session cache use {}", this.sessionCache.getClass().getName());
    }

    private String createCacheKey(String... word) {
        List<String> of = ListUtil.of(word);
        return StrUtil.join(SEPARATOR, of);
    }

    public Session createSession(UserDetail userDetail) {
        Assert.notNull(userDetail, "userDetail is not null");
        Assert.notNull(userDetail.getUserId(), "userDetail.getUserId is not null");
        String userId = userDetail.getUserId().toString();
        if (StrUtil.isBlank(userDetail.getClientId())) {
            userDetail.setClientId(DEFAULT_CLIENT);
        }
        String clientId = userDetail.getClientId();
        Session session = new Session(userId, clientId, this.securityProperties.getSessionTime());
        saveSession(session);
        saveUserDetail(userDetail);
        return session;
    }

    private void saveSession(Session session) {
        String sessionId = session.getId();
        sessionCache.put(sessionId, JacksonUtil.toJsonStr(session));
        String userId = session.getUserId();
        String clientId = session.getClientId();
        // 禁止同时登录
        if (!securityProperties.isConcurrent()) {
            String sessionsKey = createCacheKey(userId, clientId, "sessions");
            String oldSessionId = sessionCache.get(sessionsKey, String.class);
            if (oldSessionId != null) {
                removeSession(oldSessionId);
            }
            sessionCache.put(sessionsKey, sessionId);
        }
    }

    public void saveUserDetail(UserDetail userDetail) {
        String userId = userDetail.getUserId().toString();
        if (StrUtil.isBlank(userDetail.getClientId())) {
            userDetail.setClientId(DEFAULT_CLIENT);
        }
        String clientId = userDetail.getClientId();
        sessionCache.put(createCacheKey(userId, clientId), JacksonUtil.toJsonStr(userDetail));
    }


    public Session getSessionById(String sessionId) {
        String string = sessionCache.get(sessionId, String.class);
        if (string == null) {
            return null;
        }
        Session session = JacksonUtil.toBean(string, Session.class);
        if (session == null) {
            return null;
        }
        if (session.isExpired()) {
            removeSession(sessionId);
            return null;
        }
        session.renewal();
        saveSession(session);
        return session;
    }

    public void removeSession(String sessionId) {
        sessionCache.evict(sessionId);
    }

    public UserDetail getUserDetailBySessionId(String sessionId) {
        Session session = getSessionById(sessionId);
        if (session != null) {
            String userId = session.getUserId();
            String clientId = session.getClientId();
            String val = sessionCache.get(createCacheKey(userId, clientId), String.class);
            if (val == null) {
                removeSession(sessionId);
                return null;
            }
            UserDetail userDetail = JacksonUtil.toBean(val, UserDetail.class);
            if (userDetail == null) {
                removeSession(sessionId);
                return null;
            }
            return userDetail;
        }
        return null;
    }
}
