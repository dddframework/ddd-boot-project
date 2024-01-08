package com.github.ddd.tenant.spring.boot.autoconfigure;


import com.github.ddd.tenant.core.TenantDbHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ranger
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(TenantProperties.class)
public class TenantConfig {

    private final TenantProperties tenantProperties;

    @Bean
    public TenantDbHandler tenantDbHandler() {
        return new TenantDbHandler(tenantProperties);
    }
}
