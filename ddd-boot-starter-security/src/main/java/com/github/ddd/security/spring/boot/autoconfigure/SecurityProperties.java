package com.github.ddd.security.spring.boot.autoconfigure;

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
     * 会话保持时间 单位秒
     */
    private Integer sessionTime = 1800;
    /**
     * 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
     */
    private boolean concurrent = true;
    /**
     * 缓存前缀
     */
    public String sessionPrefix = "ddd:sessions";
}
