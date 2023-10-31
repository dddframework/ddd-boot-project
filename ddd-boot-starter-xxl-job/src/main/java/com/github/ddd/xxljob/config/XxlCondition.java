package com.github.ddd.xxljob.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * @author ranger
 */
public class XxlCondition implements Condition {
    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment env = context.getEnvironment();
        return env.getProperty("xxl.job.enable", Boolean.class, true);
    }
}
