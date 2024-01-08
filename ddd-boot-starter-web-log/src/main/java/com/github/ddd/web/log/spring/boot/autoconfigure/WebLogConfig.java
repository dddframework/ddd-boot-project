package com.github.ddd.web.log.spring.boot.autoconfigure;

import com.github.ddd.tenant.core.TenantDbHandler;
import com.github.ddd.tenant.spring.boot.autoconfigure.TenantProperties;
import com.github.ddd.web.log.core.LogAdvice;
import com.github.ddd.web.log.core.LogAdvisor;
import com.github.ddd.web.log.core.LogPointCut;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author ranger
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WebLogProperties.class)
public class WebLogConfig {

    private final TenantDbHandler tenantDbHandler;

    @Bean
    public LogAdvisor init(JdbcTemplate jdbcTemplate, WebLogProperties webLogProperties) {
        LogAdvisor logAdvisor = new LogAdvisor();
        logAdvisor.setLogPointCut(new LogPointCut());
        logAdvisor.setAdvice(new LogAdvice(jdbcTemplate, webLogProperties, tenantDbHandler));
        return logAdvisor;
    }
}
