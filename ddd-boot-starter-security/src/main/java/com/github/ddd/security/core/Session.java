package com.github.ddd.security.core;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * SessionId => {
 * SessionId
 * UserId
 * ClientId
 * Expires
 * CreationTime
 * LastAccessedTime
 * }
 * <p>
 * UserId + ClientId => UserDetail
 *
 * @author ranger
 */
@Data
public class Session implements Serializable {


    private String id;
    private String userId;
    private String clientId;
    private Long expiresTime;
    private Long creationTime = System.currentTimeMillis();
    private Long lastAccessedTime = this.creationTime;
    private Integer sessionTime = 1800;


    public Session(String userId, String clientId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.clientId = clientId;
    }

    public Session(String userId, String clientId, int sessionTime) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.clientId = clientId;
        this.sessionTime = sessionTime;
    }

    /**
     * 是否过期
     *
     * @return boolean
     */
    public boolean isExpired() {
        return expiresTime < System.currentTimeMillis();
    }

    /**
     * 续期
     */
    public void renewal() {
        this.lastAccessedTime = System.currentTimeMillis();
        this.expiresTime = this.lastAccessedTime + this.sessionTime * 1000;
    }
}
