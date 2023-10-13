package com.github.ddd.security.core;

import com.github.ddd.common.pojo.UserDetail;
import com.github.ddd.security.spring.boot.autoconfigure.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

/**
 * @author ranger
 */
@Slf4j
public class SessionManager {

    private final SecurityProperties securityProperties;
    protected Cache cache;

    private static final String DEFAULT_CLIENT = "default";

    public SessionManager(CacheManager cacheManager, SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.cache = cacheManager.getCache(securityProperties.getSessionPrefix());
        assert this.cache != null;
        log.info("session cache use {}", this.cache.getClass().getName());
    }

    public Session createSession(UserDetail userDetail) {
        Assert.notNull(userDetail, "userDetail is not null");
        String userId = userDetail.getUserId().toString();
        String clientId = userDetail.getClientId() == null ? DEFAULT_CLIENT : userDetail.getClientId();

        Session session = new Session(userId, clientId, this.securityProperties.getSessionTime());
        cache.put(session.getId(), session);
        cache.put(userId + ":" + clientId, userDetail);
        return session;
    }

    public void saveSession(Session session) {
        cache.put(session.getId(), session);
    }

    public void updateUserDetail(UserDetail userDetail) {
        Long userId = userDetail.getUserId();
        String clientId = userDetail.getClientId() == null ? DEFAULT_CLIENT : userDetail.getClientId();
        cache.put(userId + ":" + clientId, userDetail);
    }


    public Session getSessionById(String sessionId) {
        Session session = cache.get(sessionId, Session.class);
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
        cache.evict(sessionId);
    }

    public UserDetail getUserDetailBySessionId(String sessionId) {
        Session session = getSessionById(sessionId);
        if (session != null) {
            String userId = session.getUserId();
            String clientId = session.getClientId();
            UserDetail userDetail = cache.get(userId + ":" + clientId, UserDetail.class);
            if (userDetail == null) {
                removeSession(sessionId);
            }
            return userDetail;
        }
        return null;
    }
}
