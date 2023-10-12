package com.github.ddd.quartz.service;

import cn.hutool.core.date.DateUtil;
import com.github.ddd.common.exception.SystemException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.*;

/**
 * @author ranger
 */
@Slf4j
@RequiredArgsConstructor
public class QuartzManager {
    private final Scheduler scheduler;

    /**
     * 添加一个定时任务
     *
     * @param beanClassName Bean全路径
     * @param description   描述
     * @param cron          cron表达式
     */
    public void addJob(String beanClassName, String description, String cron) {
        Class<?> clazz;
        try {
            clazz = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new SystemException("执行类不存在");
        }
        if (!QuartzJobBean.class.isAssignableFrom(clazz)) {
            throw new SystemException("执行类不是QuartzJobBean的子类");
        }
        String name = UUID.randomUUID().toString();
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends org.quartz.Job>) clazz)
                .withIdentity(name, Scheduler.DEFAULT_GROUP)
                .withDescription(description)
                .storeDurably()
                .build();

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name, Scheduler.DEFAULT_GROUP)
                .forJob(jobDetail)
                .withSchedule(cronScheduleBuilder)
                .startNow()
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("定时任务添加失败", e);
            throw new SystemException("定时任务添加失败", e);
        }
    }

    /**
     * 更新定时任务 Cron表达式
     *
     * @param jobName
     * @param cron
     */
    public void updateCornJob(String jobName, String cron) {
        try {
            TriggerKey triggerKey = new TriggerKey(jobName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("定时任务更新失败", e);
            throw new SystemException("定时任务更新失败", e);
        }
    }

    /**
     * 删除定时任务
     *
     * @param jobName
     */
    public void removeJob(String jobName) {
        try {
            scheduler.deleteJob(new JobKey(jobName));
        } catch (SchedulerException e) {
            log.error("定时任务删除失败", e);
            throw new SystemException("定时任务删除失败", e);
        }
    }

    /**
     * 暂停定时任务
     *
     * @param jobKey
     */
    public void pauseJob(JobKey jobKey) {
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error("定时任务暂停失败", e);
            throw new SystemException("定时任务暂停失败", e);
        }
    }

    /**
     * 暂停全部定时任务
     */
    public void pauseAllJob() {
        try {
            scheduler.pauseAll();
        } catch (SchedulerException e) {
            log.error("定时任务暂停失败", e);
            throw new SystemException("定时任务暂停失败", e);
        }
    }

    /**
     * 恢复定时任务
     *
     * @param jobName
     */
    public void resumeJob(String jobName) {
        try {
            scheduler.resumeJob(new JobKey(jobName));
        } catch (SchedulerException e) {
            log.error("定时任务恢复失败", e);
            throw new SystemException("定时任务恢复失败", e);
        }
    }

    /**
     * 恢复全部定时任务
     */
    public void resumeAllJob() {
        try {
            scheduler.resumeAll();
        } catch (SchedulerException e) {
            log.error("定时任务恢复失败", e);
            throw new SystemException("定时任务恢复失败", e);
        }
    }

    /**
     * 触发定时任务
     *
     * @param jobName
     */
    public void triggerJob(String jobName) {
        try {
            scheduler.triggerJob(new JobKey(jobName));
        } catch (SchedulerException e) {
            log.error("定时任务触发失败", e);
            throw new SystemException("定时任务触发失败", e);
        }
    }

    public List<Job> list() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("BLOCKED", "阻塞");
            map.put("COMPLETE", "完成");
            map.put("ERROR", "出错");
            map.put("NONE", "不存在");
            map.put("NORMAL", "正常");
            map.put("PAUSED", "暂停");
            List<Job> list = new ArrayList<>();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
                        Job item = new Job();
                        item.setName(jobKey.getName());
                        item.setBeanName(jobDetail.getJobClass().getName());
                        item.setDescription(jobDetail.getDescription());
                        item.setDisallowConcurrentExecution(jobDetail.isConcurrentExectionDisallowed());
                        item.setNextFireTime(DateUtil.formatDateTime(trigger.getNextFireTime()));
                        item.setTriggerType(trigger.getClass().getSimpleName());
                        if (trigger instanceof CronTrigger) {
                            item.setCron(((CronTrigger) trigger).getCronExpression());
                        }
                        item.setMisfireInstruction(trigger.getMisfireInstruction());
                        String state = scheduler.getTriggerState(trigger.getKey()).name();
                        item.setState(map.get(state));
                        list.add(item);
                    }
                }
            }
            return list;
        } catch (SchedulerException e) {
            log.error("定时任务查询失败", e);
            throw new SystemException("定时任务查询失败" + e.getMessage());
        }
    }

    @Data
    public static class Job {
        private String name;
        private String cron;
        private String beanName;
        private String description;
        private String triggerType;
        private String nextFireTime;
        private int misfireInstruction;
        private boolean disallowConcurrentExecution;
        private String state;
    }
}
