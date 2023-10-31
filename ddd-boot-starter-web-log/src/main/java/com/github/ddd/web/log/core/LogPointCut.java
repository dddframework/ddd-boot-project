package com.github.ddd.web.log.core;


import lombok.SneakyThrows;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

/**
 * @author ranger
 */
public class LogPointCut extends StaticMethodMatcherPointcut {

    /**
     * SneakyThrows
     *
     * @param method
     * @param aClass
     * @return
     */
    @SneakyThrows
    @Override
    public boolean matches(@NonNull Method method, @NonNull Class<?> aClass) {
        return AnnotatedElementUtils.hasAnnotation(method, BizLog.class);
    }
}
