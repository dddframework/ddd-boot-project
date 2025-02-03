package com.github.ddd.mybatis.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "ddd.mybatis")
public class MybatisProperties {

    /**
     * 是否开启多租户模式
     */
    private boolean enableSaas = false;
    /**
     * Schema 前缀
     */
    private String schemaPrefix = "";

    /**
     * TinyId 数据表名
     */
    private String tinyIdTable = "tiny_id";


    /**
     * Schema 前缀
     */
    private String bizLogTable = "web_log";
}
