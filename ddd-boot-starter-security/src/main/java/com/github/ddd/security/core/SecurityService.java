package com.github.ddd.security.core;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.github.ddd.common.util.JacksonUtil;
import com.github.ddd.security.config.SecurityProperties;
import com.github.ddd.security.pojo.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 安全服务
 *
 * @author ranger
 */
@RequiredArgsConstructor
public class SecurityService {


    private static final String ID_KEY = "login:%s:%s";
    private static final String TOKEN_KEY = "token:%s";
    private static final String DEFAULT_CLIENT = "default";

    private final StringRedisTemplate stringRedisTemplate;

    private final SecurityProperties securityProperties;

    /**
     * 获取会话时间
     *
     * @return sessionTime
     */
    public Long getSessionTime() {
        Long sessionTime = securityProperties.getSessionTime();
        return sessionTime == null ? 24L : sessionTime;
    }

    /**
     * 退出登录
     *
     * @param userId   用户ID
     * @param clientId 客户端
     */
    public void logout(String userId, String clientId) {
        String loginKey = String.format(ID_KEY, userId, clientId);
        String token = stringRedisTemplate.opsForValue().get(loginKey);
        if (StrUtil.isNotBlank(token)) {
            String tokenKey = TOKEN_KEY + token;
            stringRedisTemplate.delete(tokenKey);
            stringRedisTemplate.delete(loginKey);
        }
    }

    /**
     * 登录
     *
     * @param user 用户信息
     * @return token
     */
    @Lock4j(keys = {"#user.userId"}, name = "login")
    public String login(UserDetail user) {
        String userId = user.getUserId();
        String clientId = user.getClientId();
        if (StrUtil.isBlank(clientId)){
            clientId = DEFAULT_CLIENT;
        }
        this.logout(userId, clientId);
        String loginKey = String.format(ID_KEY, userId, clientId);
        String token = IdUtil.fastSimpleUUID();
        String tokenKey = String.format(TOKEN_KEY, token);
        Long sessionTime = getSessionTime();
        stringRedisTemplate.opsForValue().set(tokenKey, JacksonUtil.toJsonStr(user), sessionTime, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(loginKey, token, sessionTime, TimeUnit.HOURS);
        return token;
    }


    /**
     * 续期
     *
     * @param userId   用户ID
     * @param clientId 客户端
     */
    private void renewal(String userId, String clientId) {
        String loginKey = String.format(ID_KEY, userId, clientId);
        String token = stringRedisTemplate.opsForValue().get(loginKey);
        if (StrUtil.isNotBlank(token)) {
            String tokenKey = String.format(TOKEN_KEY, token);
            Long sessionTime = getSessionTime();
            stringRedisTemplate.expire(loginKey, sessionTime, TimeUnit.HOURS);
            stringRedisTemplate.expire(tokenKey, sessionTime, TimeUnit.HOURS);
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
        String str = stringRedisTemplate.opsForValue().get(tokenKey);
        if (StrUtil.isNotBlank(str)) {
            UserDetail data = JacksonUtil.toBean(str, UserDetail.class);
            String userId = data.getUserId();
            String clientId = data.getClientId();
            this.renewal(userId, clientId);
            return data;
        }
        return null;
    }
}
