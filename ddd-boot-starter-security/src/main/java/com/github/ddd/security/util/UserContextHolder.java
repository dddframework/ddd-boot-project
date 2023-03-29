package com.github.ddd.security.util;

import com.github.ddd.security.pojo.UserDetail;

/**
 * 用户信息工具类
 *
 * @author ranger
 */
public class UserContextHolder {

    private static final ThreadLocal<UserDetail> USER_CONTEXT = new ThreadLocal<>();

    /**
     * 设置用户上下文
     *
     * @param user 用户
     */
    public static void setUserContext(UserDetail user) {
        USER_CONTEXT.remove();
        USER_CONTEXT.set(user);
    }

    /**
     * 获取User
     *
     * @return UserDetail
     */
    public static UserDetail getCurrentUser() {
        return USER_CONTEXT.get();
    }

    /**
     * 获取当前登录用户ID
     *
     * @return UserId
     */
    public static Long getUserId() {
        return USER_CONTEXT.get().getUserId();
    }

    /**
     * 获取当前登录用户姓名
     *
     * @return Nickname
     */
    public static String getNickname() {
        return USER_CONTEXT.get().getNickname();
    }


    /**
     * 获取当前登录用户租户ID
     *
     * @return TenantId
     */
    public static Long getTenantId() {
        return USER_CONTEXT.get().getTenantId();
    }
}
