package com.github.ddd.microservice.config;

import com.github.ddd.microservice.exception.GlobalExceptionHandler;
import com.github.ddd.microservice.feign.FeignRequestInterceptor;
import feign.RequestInterceptor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author ranger
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {


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

    /**
     * 全局异常拦截
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }


    /**
     * knife4j-放开Swagger文档
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
