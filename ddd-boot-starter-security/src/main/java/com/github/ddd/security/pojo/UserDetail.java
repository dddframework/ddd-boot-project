package com.github.ddd.security.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
     * 拥有权限码
     */
    private Set<String> authCodes = new HashSet<>();
    /**
     * 用户所属租户
     */
    private Tenant tenant;
    /**
     * 用户部门
     */
    private Set<Group> groups = new HashSet<>();
    /**
     * 用户角色
     */
    private Set<Role> roles = new HashSet<>();
    /**
     * 其他属性
     */
    private Map<String, Object> properties = new HashMap<>();

    /**
     * 部门
     */
    @EqualsAndHashCode
    @Data
    public static class Group {
        private Long id;
        private String name;
    }

    /**
     * 角色
     */
    @EqualsAndHashCode
    @Data
    public static class Role {
        private Long id;
        private String name;
    }

    /**
     * 租户
     */
    @EqualsAndHashCode
    @Data
    public static class Tenant {
        private Long id;
        private String name;
    }
}
