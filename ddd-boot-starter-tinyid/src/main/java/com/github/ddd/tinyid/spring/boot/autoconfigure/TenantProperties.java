package com.github.ddd.tinyid.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据库租户模式配置
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
    /**
     * TinyId 数据表名
     */
    private String tinyIdTable = "tiny_id";
}
