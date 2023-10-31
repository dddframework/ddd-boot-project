package com.github.ddd.web.log.core;

import java.lang.annotation.*;

/**
 * 业务日志
 *
 * @author ranger
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizLog {
    /**
     * 业务名称
     *
     * @return
     */
    String value();

    /**
     * 是否记录方法参数
     *
     * @return
     */
    boolean recordParam() default true;
}
