package com.github.ddd.tinyid.spring.boot.autoconfigure;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.github.ddd.tinyid.core.SegmentIdService;
import com.github.ddd.tinyid.core.TinyIdGeneratorFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author ranger
 */
@EnableSpringUtil
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class TinyIdConfig {

    @Bean
    public SegmentIdService segmentIdService(JdbcTemplate jdbcTemplate, TenantProperties tenantProperties) {
        return new SegmentIdService(jdbcTemplate, tenantProperties);
    }

    @Bean
    public TinyIdGeneratorFactory tinyIdGeneratorFactory(SegmentIdService segmentIdService) {
        return new TinyIdGeneratorFactory(segmentIdService);
    }
}
