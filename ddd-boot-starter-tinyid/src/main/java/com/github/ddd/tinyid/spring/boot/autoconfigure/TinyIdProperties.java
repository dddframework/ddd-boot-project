package com.github.ddd.tinyid.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据库租户模式配置
 *
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "ddd.tinyid")
public class TinyIdProperties {
    /**
     * TinyId 数据表名
     */
    private String tinyIdTable = "tiny_id";
}
