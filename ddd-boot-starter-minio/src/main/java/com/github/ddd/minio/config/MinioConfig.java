package com.github.ddd.minio.config;


import com.github.ddd.minio.core.MinioService;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ranger
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioProperties properties) {
        MinioClient.Builder builder = MinioClient.builder();
        builder.endpoint(properties.getUrl());
        builder.credentials(properties.getAccessKey(), properties.getSecretKey());
        return builder.build();
    }

    @Bean
    public MinioService minioService(MinioClient minioClient, MinioProperties properties) {
        return new MinioService(minioClient, properties);
    }
}
