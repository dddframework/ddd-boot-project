package com.github.ddd.web.config;

import com.github.ddd.web.exception.GlobalExceptionHandler;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ranger
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 如果不添加这个 GroupedOpenApi 实例，knife4j ui就显示不出来。
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
    }

    /**
     * 全局异常拦截
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
