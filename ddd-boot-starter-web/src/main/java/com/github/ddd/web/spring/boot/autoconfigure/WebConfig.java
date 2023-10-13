package com.github.ddd.web.spring.boot.autoconfigure;

import com.github.ddd.web.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ranger
 */
@Configuration
public class WebConfig {


    /**
     * 全局异常处理
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
