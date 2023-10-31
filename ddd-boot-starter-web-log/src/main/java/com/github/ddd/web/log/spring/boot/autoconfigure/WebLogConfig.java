package com.github.ddd.web.log.spring.boot.autoconfigure;

import com.github.ddd.web.log.core.LogAdvice;
import com.github.ddd.web.log.core.LogAdvisor;
import com.github.ddd.web.log.core.LogPointCut;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author ranger
 */
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class WebLogConfig {

    @Bean
    public LogAdvisor init(JdbcTemplate jdbcTemplate, TenantProperties tenantProperties) {
        LogAdvisor logAdvisor = new LogAdvisor();
        logAdvisor.setLogPointCut(new LogPointCut());
        logAdvisor.setAdvice(new LogAdvice(jdbcTemplate, tenantProperties));
        return logAdvisor;
    }
}
