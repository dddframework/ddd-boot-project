package com.github.ddd.quartz;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import java.util.Date;

/**
 * Job监听器
 *
 * @author ranger
 */
@Slf4j
public class GlobalJobListener implements JobListener {


    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        String description = jobDetail.getDescription();
        String now = DateUtil.formatDateTime(new Date());
        log.info("【{}】 开始执行，当前时间: {}", description, now);
    }

    /**
     * 任务意外中止
     *
     * @param context
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String description = context.getJobDetail().getDescription();
        String now = DateUtil.formatDateTime(new Date());
        log.info("【{}】 意外中止，当前时间: {}", description, now);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        String description = context.getJobDetail().getDescription();
        String now = DateUtil.formatDateTime(new Date());
        if (jobException != null) {
            log.error("【{}】 执行出现异常，当前时间: {}", description, now, jobException);
        } else {
            log.info("【{}】 结束执行，当前时间: {}", description, now);
        }
    }
}
