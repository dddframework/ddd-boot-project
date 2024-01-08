package com.github.ddd.tinyid.spring.boot.autoconfigure;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.github.ddd.tenant.core.TenantDbHandler;
import com.github.ddd.tinyid.core.SegmentIdService;
import com.github.ddd.tinyid.core.TinyIdGeneratorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author ranger
 */
@EnableSpringUtil
@Configuration
@EnableConfigurationProperties(TinyIdProperties.class)
@RequiredArgsConstructor
public class TinyIdConfig {

    private final TenantDbHandler tenantDbHandler;

    @Bean
    public SegmentIdService segmentIdService(JdbcTemplate jdbcTemplate, TinyIdProperties tinyIdProperties) {
        return new SegmentIdService(jdbcTemplate, tinyIdProperties, tenantDbHandler);
    }

    @Bean
    public TinyIdGeneratorFactory tinyIdGeneratorFactory(SegmentIdService segmentIdService) {
        return new TinyIdGeneratorFactory(segmentIdService);
    }
}
