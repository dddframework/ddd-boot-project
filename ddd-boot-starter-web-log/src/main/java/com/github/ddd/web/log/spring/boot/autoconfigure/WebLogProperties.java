package com.github.ddd.web.log.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "ddd.log")
public class WebLogProperties {

    /**
     * Schema 前缀
     */
    private String bizLogTable = "sys_log";
}
