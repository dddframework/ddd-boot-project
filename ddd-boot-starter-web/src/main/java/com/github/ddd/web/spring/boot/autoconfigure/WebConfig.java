package com.github.ddd.web.spring.boot.autoconfigure;

import com.github.ddd.web.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @author ranger
 */
@RequiredArgsConstructor
@Configuration
@EnableWebMvc
@EnableConfigurationProperties(LocalFileConfig.class)
public class WebConfig implements WebMvcConfigurer {


    private final LocalFileConfig localFileConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/f/**")
                .addResourceLocations("file:" + localFileConfig.getRoot())
                .setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS).cachePublic());
    }

    /**
     * 全局异常处理
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
