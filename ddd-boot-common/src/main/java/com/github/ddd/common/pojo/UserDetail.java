package com.github.ddd.common.pojo;

import lombok.Data;

import java.io.Serializable;
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
public class UserDetail implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名/登录账户
     */
    private String username;
    /**
     * 用户姓名/昵称
     */
    private String nickname;
    /**
     * 登录客户端
     */
    private String clientId;
    /**
     * 租户ID
     */
    private Long tenantId;
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
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(0L);
        userDetail.setUsername("系统");
        userDetail.setNickname("系统");
        return userDetail;
    }


    /**
     * 创建系统用户
     *
     * @return UserDetail
     */
    public static UserDetail createSystemUser(Long tenantId) {
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(0L);
        userDetail.setUsername("系统");
        userDetail.setNickname("系统");
        userDetail.setTenantId(tenantId);
        return userDetail;
    }
}
