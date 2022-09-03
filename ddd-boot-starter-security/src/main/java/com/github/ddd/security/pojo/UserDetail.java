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
    private Long userId;
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

    /**
     * 创建系统用户
     *
     * @return UserDetail
     */
    public static UserDetail createSystemUser() {
        return createCustomUser(0L, "系统");
    }

    /**
     * 创建自定义用户
     *
     * @return UserDetail
     */
    public static UserDetail createCustomUser(Long userId, String username) {
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(userId);
        userDetail.setUsername(username);
        return userDetail;
    }
}
