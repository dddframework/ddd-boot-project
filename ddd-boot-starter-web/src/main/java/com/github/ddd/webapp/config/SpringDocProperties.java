package com.github.ddd.webapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringDoc配置
 *
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "spring.doc")
public class SpringDocProperties {

    /**
     * 模块描述
     */
    private String description;
}