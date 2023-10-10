package com.github.ddd.quartz;

import org.quartz.JobListener;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author ranger
 */
@Component
public class SchedulerFactoryBeanCustomizerImpl implements SchedulerFactoryBeanCustomizer {

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        JobListener jobListener = new GlobalJobListener();
        schedulerFactoryBean.setGlobalJobListeners(jobListener);
    }
}
