package com.github.ddd.jdbc.spring.boot.autoconfigure;

import com.github.ddd.jdbc.core.AdvancedQueryTemplate;
import com.github.ddd.jdbc.core.AdvancedSqlFactory;
import com.github.ddd.tenant.spring.boot.autoconfigure.TenantProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author ranger
 */
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AdvancedQueryProperties.class)
public class AdvancedQueryConfig {

    private final TenantProperties tenantProperties;
    private final AdvancedQueryProperties advancedQueryProperties;


    @Bean
    public AdvancedSqlFactory advancedSqlFactory() {
        return new AdvancedSqlFactory(advancedQueryProperties.getLocations());
    }

    @Bean
    public AdvancedQueryTemplate advancedQueryTemplate(JdbcTemplate jdbcTemplate, AdvancedSqlFactory advancedSqlFactory) {
        return new AdvancedQueryTemplate(jdbcTemplate, tenantProperties, advancedSqlFactory);
    }
}
