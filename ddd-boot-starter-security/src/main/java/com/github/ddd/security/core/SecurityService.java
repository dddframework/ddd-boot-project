package com.github.ddd.security.core;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.github.ddd.common.util.JacksonUtil;
import com.github.ddd.security.pojo.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 安全服务
 *
 * @author ranger
 */
@Service
@RequiredArgsConstructor
public class SecurityService {


    private static final String ID_KEY = "login:";
    private static final String TOKEN_KEY = "token:";

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 退出登录
     *
     * @param userId   用户ID
     * @param clientId 客户端
     */
    public void logout(String userId, String clientId) {
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
     * @param user 用户信息
     * @return
     */
    @Lock4j(keys = {"#user.userId", "#user.clientId"}, name = "login")
    public String login(UserDetail user) {
        String userId = user.getUserId();
        String clientId = user.getClientId();
        this.logout(userId, clientId);
        String loginKey = ID_KEY + clientId + ":" + userId;
        String token = IdUtil.fastSimpleUUID();
        String tokenKey = TOKEN_KEY + token;
        stringRedisTemplate.opsForValue().set(tokenKey, JacksonUtil.toJsonStr(user), 12L, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(loginKey, token, 12L, TimeUnit.HOURS);
        return token;
    }


    /**
     * 续期
     *
     * @param userId   用户ID
     * @param clientId 客户端
     */
    public void renewal(String userId, String clientId) {
        String loginKey = ID_KEY + clientId + ":" + userId;
        String token = stringRedisTemplate.opsForValue().get(loginKey);
        if (StrUtil.isNotBlank(token)) {
            String tokenKey = TOKEN_KEY + token;
            stringRedisTemplate.expire(loginKey, 12L, TimeUnit.HOURS);
            stringRedisTemplate.expire(tokenKey, 12L, TimeUnit.HOURS);
        }
    }

    /**
     * 解析用户信息
     *
     * @param token 令牌
     * @return Token
     */
    public UserDetail parseToken(String token) {
        String tokenKey = TOKEN_KEY + token;
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
