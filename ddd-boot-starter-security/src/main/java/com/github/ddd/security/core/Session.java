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
    private Long creationTime;
    private Long lastAccessedTime;
    private Integer sessionTime = 1800;

    public Session() {

    }

    public Session(String userId, String clientId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.clientId = clientId;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
    }

    public Session(String userId, String clientId, int sessionTime) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.clientId = clientId;
        this.sessionTime = sessionTime;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
        this.expiresTime = this.lastAccessedTime + this.sessionTime * 1000;
    }

    /**
     * 是否过期
     *
     * @return boolean
     */
    public boolean isExpired() {
        return expiresTime <= System.currentTimeMillis();
    }

    /**
     * 续期
     */
    public void renewal() {
        this.lastAccessedTime = System.currentTimeMillis();
        this.expiresTime = this.lastAccessedTime + this.sessionTime * 1000;
    }
}
