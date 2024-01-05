package com.github.ddd.jdbc.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "advanced-query")
public class AdvancedQueryProperties {
    /**
     * Locations of SQL Files.
     */
    private String[] locations = new String[]{"classpath*:/advanced-query/**/*.xml"};
}
