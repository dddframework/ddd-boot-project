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
     * 会话保持时间 单位小时
     */
    private Long sessionTime = 12L;
    /**
     * 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
     */
    private boolean concurrent = true;
}
