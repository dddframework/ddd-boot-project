package com.github.ddd.security.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用户信息
 *
 * @author ranger
 */
@Data
public class UserDetail {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录客户端
     */
    private String clientId;
    /**
     * 拥有权限码
     */
    private Set<String> authCodes = new HashSet<>();
    /**
     * 其他属性
     */
    private Map<String, Object> properties = new HashMap<>();
}
