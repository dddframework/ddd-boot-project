package com.github.ddd.security.core;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.util.JacksonUtil;
import com.github.ddd.security.config.SecurityProperties;
import com.github.ddd.security.pojo.UserDetail;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 安全服务
 *
 * @author ranger
 */
@RequiredArgsConstructor
public class SecurityService {


    private static final String ID_KEY = "login_id:%s:%s";
    private static final String TOKEN_KEY = "login_token:%s";
    private static final String DEFAULT_CLIENT = "default";


    private final SessionManager sessionManager;

    private final SecurityProperties securityProperties;

    /**
     * 退出登录
     *
     * @param userId 用户ID
     */
    public void logout(Long userId) {
        logout(userId, DEFAULT_CLIENT);
    }

    /**
     * 退出登录
     *
     * @param userId   用户ID
     * @param clientId 客户端
     */
    public void logout(Long userId, String clientId) {
        String loginKey = String.format(ID_KEY, userId, clientId);
        String token = sessionManager.get(loginKey);
        if (StrUtil.isNotBlank(token)) {
            String tokenKey = String.format(TOKEN_KEY, token);
            sessionManager.remove(tokenKey);
            sessionManager.remove(loginKey);
        }
    }

    /**
     * 登录
     *
     * @param user 用户信息
     * @return token
     */
    public String login(UserDetail user) {
        if (StrUtil.isBlank(user.getClientId())) {
            user.setClientId(DEFAULT_CLIENT);
        }
        Long userId = user.getUserId();
        String clientId = user.getClientId();
        if (!securityProperties.isConcurrent()) {
            this.logout(userId, clientId);
        }
        String loginKey = String.format(ID_KEY, userId, clientId);
        String token = IdUtil.fastSimpleUUID();
        String tokenKey = String.format(TOKEN_KEY, token);
        Long sessionTime = securityProperties.getSessionTime();
        sessionManager.put(tokenKey, user, sessionTime, TimeUnit.HOURS);
        sessionManager.put(loginKey, token, sessionTime, TimeUnit.HOURS);
        return token;
    }


    /**
     * 续期
     *
     * @param userId   用户ID
     * @param clientId 客户端
     */
    private void renewal(Long userId, String clientId) {
        String loginKey = String.format(ID_KEY, userId, clientId);
        String token = sessionManager.get(loginKey);
        if (StrUtil.isNotBlank(token)) {
            String tokenKey = String.format(TOKEN_KEY, token);
            Long sessionTime = securityProperties.getSessionTime();
            sessionManager.expire(loginKey, sessionTime, TimeUnit.HOURS);
            sessionManager.expire(tokenKey, sessionTime, TimeUnit.HOURS);
        }
    }

    /**
     * 解析用户信息
     *
     * @param token 令牌
     * @return UserDetail
     */
    public UserDetail parseToken(String token) {
        String tokenKey = String.format(TOKEN_KEY, token);
        String str = sessionManager.get(tokenKey);
        if (StrUtil.isNotBlank(str)) {
            UserDetail data = JacksonUtil.toBean(str, UserDetail.class);
            Long userId = data.getUserId();
            String clientId = data.getClientId();
            this.renewal(userId, clientId);
            return data;
        }
        return null;
    }
}
