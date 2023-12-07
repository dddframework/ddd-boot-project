package com.github.ddd.web.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ranger
 * @version 1.0
 */
@Data
@ConfigurationProperties(prefix = "ddd.local-file")
public class LocalFileConfig {

    /**
     * 根目录
     */
    private String root = "/file/";
    /**
     * 文件域
     */
    private String domain = "http://127.0.0.1/f";
}
