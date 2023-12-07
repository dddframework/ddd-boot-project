package com.github.ddd.tenant.spring.boot.autoconfigure;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ranger
 */
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class TenantConfig {

}
