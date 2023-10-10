package com.github.ddd.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mybatis动态表名配置
 *
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "tenant-mode")
public class TenantProperties {

    /**
     * 是否开启多租户模式
     */
    private boolean enable = false;
    /**
     * Schema 前缀
     */
    private String schemaPrefix = "";
}
