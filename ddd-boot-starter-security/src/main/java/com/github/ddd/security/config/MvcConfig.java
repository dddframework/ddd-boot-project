package com.github.ddd.security.config;

import com.github.ddd.security.filter.PermissionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 研发中心-彭幸园
 * @version 1.0
 * @packageName com.shocksoft.component.user.config
 * @fileName MvcConfig
 * @createTime 2022/8/17 10:11
 * @Copyright © 2021 Shocksoft
 */
@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/rpc/**", "/v2/**");
    }
}
