package com.github.ddd.web.log.core;

import lombok.Setter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lang.NonNull;

/**
 * @author ranger
 */
public class LogAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    @Setter
    private Pointcut logPointCut;

    @NonNull
    @Override
    public Pointcut getPointcut() {
        return logPointCut;
    }

}
