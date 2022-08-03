package com.github.ddd.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mybatis动态表名配置
 *
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "saas-db")
public class SaasDbProperties {

    /**
     * 是否开启SAAS 模式
     */
    private boolean enable = false;
    /**
     * Schema 前缀
     */
    private String prefix = "";
}