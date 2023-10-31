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
     * 文件域名前缀
     */
    private String domain = "http://127.0.0.1";

    /**
     * Minio 地址
     */
    private String url = "http://127.0.0.1:9000";

    /**
     * Minio access-key 默认
     */
    private String accessKey = "minioadmin";

    /**
     * Minio secretKey
     */
    private String secretKey = "minioadmin";
}
