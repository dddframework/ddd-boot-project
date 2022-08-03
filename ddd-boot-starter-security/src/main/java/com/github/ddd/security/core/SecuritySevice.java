package com.github.ddd.security.core;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.github.ddd.common.util.JacksonUtil;
import com.github.ddd.security.pojo.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 安全服务
 * @author ranger
 */
@Service
@RequiredArgsConstructor
public class SecuritySevice {


    private static final String ID_KEY = "login:";
    private static final String TOKEN_KEY = "token:";

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 退出登录
     *
     * @param userId   用户ID
     * @param clientId 客户端
     */
    public void logout(Long userId, String clientId) {
        String loginKey = ID_KEY + clientId + ":" + userId;
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
     * @param userId   用户ID
     * @param clientId 客户端
     * @return token
     */
    @Lock4j(keys = {"#userId", "#clientId"}, name = "login")
    public String login(Long userId, String clientId) {
        this.logout(userId, clientId);
        String loginKey = ID_KEY + clientId + ":" + userId;
        String token = IdUtil.fastSimpleUUID();
        String tokenKey = TOKEN_KEY + token;
        Token dto = new Token(userId, clientId);
        stringRedisTemplate.opsForValue().set(tokenKey, JacksonUtil.toJsonStr(dto), 2L, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(loginKey, token, 2L, TimeUnit.HOURS);
        return token;
    }


    /**
     * 续期
     *
     * @param userId   用户ID
     * @param clientId 客户端
     */
    public void renewal(Long userId, String clientId) {
        String loginKey = ID_KEY + clientId + ":" + userId;
        String token = stringRedisTemplate.opsForValue().get(loginKey);
        if (StrUtil.isNotBlank(token)) {
            String tokenKey = TOKEN_KEY + token;
            stringRedisTemplate.expire(loginKey, 2L, TimeUnit.HOURS);
            stringRedisTemplate.expire(tokenKey, 2L, TimeUnit.HOURS);
        }
    }

    /**
     * 解析用户信息
     *
     * @param token 令牌
     * @return Token
     */
    public Token parseToken(String token) {
        String tokenKey = TOKEN_KEY + token;
        String str = stringRedisTemplate.opsForValue().get(tokenKey);
        if (StrUtil.isNotBlank(str)) {
            Token data = JacksonUtil.toBean(str, Token.class);
            Long userId = data.getUserId();
            String clientId = data.getClientId();
            this.renewal(userId, clientId);
            return data;
        }
        return null;
    }
}
