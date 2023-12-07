package com.github.ddd.tenant.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "ddd.tenant")
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
