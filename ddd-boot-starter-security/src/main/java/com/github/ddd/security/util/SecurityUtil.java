package com.github.ddd.security.util;

import com.github.ddd.security.pojo.UserDetail;

/**
 * 用户信息工具类
 *
 * @author ranger
 */
public class SecurityUtil {

    private static final ThreadLocal<UserDetail> USER_CONTEXT = new ThreadLocal<>();

    /**
     * 设置用户上下文
     *
     * @param user
     */
    public static void setUserContext(UserDetail user) {
        USER_CONTEXT.remove();
        USER_CONTEXT.set(user);
    }

    /**
     * 防止内存泄漏
     */
    public static void remove() {
        USER_CONTEXT.remove();
    }

    /**
     * 获取User
     *
     * @return
     */
    public static UserDetail getCurrentUser() {
        return USER_CONTEXT.get();
    }

    /**
     * 获取当前登录用户ID
     *
     * @return
     */
    public static String getUserId() {
        return USER_CONTEXT.get().getUserId();
    }


}
