package com.github.ddd.quartz.annotation;

import org.quartz.CronTrigger;

import java.lang.annotation.*;

/**
 * 定时任务
 *
 * @author ranger
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CronJob {
    /**
     * 任务描述
     */
    String description();

    /**
     * 定时表达式
     */
    String cron();

    /**
     * 容错
     * 参考 {@link org.quartz.CronTrigger}
     * 参考 {@link org.quartz.Trigger}
     */
    int misfireInstruction() default CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING;
}
