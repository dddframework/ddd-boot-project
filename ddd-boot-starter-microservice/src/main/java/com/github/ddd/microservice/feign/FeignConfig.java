package com.github.ddd.microservice.feign;

import feign.RequestInterceptor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Feign 配置
 *
 * @author ranger
 */
@Configuration
public class FeignConfig {


    @Value("${feign.headers}")
    private List<String> headers;

    @Bean
    RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor(headers);
    }


    /**
     * 如果不添加这个 GroupedOpenApi 实例，knife4j ui就显示不出来。
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
    }
}