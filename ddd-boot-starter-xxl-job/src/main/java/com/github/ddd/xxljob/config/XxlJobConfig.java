package com.github.ddd.xxljob.config;


import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * XxlJobConfig
 *
 * @author ranger
 */
@Slf4j
@Configuration
public class XxlJobConfig {

    @Value("${xxl.job.enable:true}")
    private Boolean enable;

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean
    @Conditional(XxlCondition.class)
    public XxlJobSpringExecutor xxlJobExecutor() {
        if (enable) {
            log.info("xxl-job 配置初始化。。。。。。。。。。。。。。。。。");
        } else {
            log.warn("xxl-job 未启用。。。。。。。。。。。。。。。。。。。");
        }
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }
}
