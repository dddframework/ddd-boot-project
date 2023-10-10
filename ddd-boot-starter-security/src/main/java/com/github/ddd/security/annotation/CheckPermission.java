package com.github.ddd.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 *
 * @author ranger
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CheckPermission {
    /**
     * 需要校验的权限码
     *
     * @return 需要校验的权限码
     */
    String[] value() default {};

    /**
     * 校验策略 AND OR
     *
     * @return boolean
     */
    boolean and() default true;

}
