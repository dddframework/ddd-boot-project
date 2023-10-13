package com.github.ddd.tinyid.spring.boot.autoconfigure;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.github.ddd.tinyid.core.SegmentIdService;
import com.github.ddd.tinyid.core.TinyIdGeneratorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author ranger
 */
@EnableSpringUtil
@Configuration
public class TinyIdConfig {

    @Bean
    public SegmentIdService segmentIdService(JdbcTemplate jdbcTemplate) {
        return new SegmentIdService(jdbcTemplate);
    }

    @Bean
    public TinyIdGeneratorFactory tinyIdGeneratorFactory(SegmentIdService segmentIdService) {
        return new TinyIdGeneratorFactory(segmentIdService);
    }
}
