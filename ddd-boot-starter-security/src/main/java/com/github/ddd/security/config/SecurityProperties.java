package com.github.ddd.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    /**
     * 白名单
     */
    private List<String> whiteList;
    /**
     * 会话保持时间
     */
    private Long sessionTime;
}
