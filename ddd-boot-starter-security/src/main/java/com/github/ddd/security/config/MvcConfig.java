package com.github.ddd.security.config;

import cn.hutool.core.collection.CollUtil;
import com.github.ddd.security.core.SecurityService;
import com.github.ddd.security.filter.PermissionInterceptor;
import com.github.ddd.security.filter.UserContextFilter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
@Order(99)
@EnableWebMvc
@EnableConfigurationProperties(SecurityProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;


    @Bean
    public SecurityService securityService(StringRedisTemplate stringRedisTemplate){
        return new SecurityService(stringRedisTemplate, securityProperties);
    }

    @Bean
    public UserContextFilter userContextFilter(SecurityService securityService){
        return new UserContextFilter(securityService);
    }

    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> whiteList = securityProperties.getWhiteList();
        InterceptorRegistration registration = registry.addInterceptor(permissionInterceptor())
                .addPathPatterns("/**");
        if (CollUtil.isNotEmpty(whiteList)) {
            registration.excludePathPatterns(whiteList);
        }
    }
}
