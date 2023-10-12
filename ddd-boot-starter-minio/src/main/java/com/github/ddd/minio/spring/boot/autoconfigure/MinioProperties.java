package com.github.ddd.minio.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ranger
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 指定域名
     */
    private String domain;

    /**
     * URL
     */
    private String url;

    /**
     * access-key 默认
     */
    private String accessKey = "minioadmin";

    /**
     * secretKey
     */
    private String secretKey = "minioadmin";
}
