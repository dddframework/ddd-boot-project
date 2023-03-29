package com.github.ddd.security.config;

import cn.hutool.core.collection.CollUtil;
import com.github.ddd.security.core.SecurityService;
import com.github.ddd.security.filter.PermissionInterceptor;
import com.github.ddd.security.filter.UserContextFilter;
import lombok.RequiredArgsConstructor;
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
 * @author ranger
 */
@RequiredArgsConstructor
@Configuration
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
        InterceptorRegistration registration = registry
                .addInterceptor(permissionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/v3/api-docs/**");
        if (CollUtil.isNotEmpty(whiteList)) {
            registration.excludePathPatterns(whiteList);
        }
    }
}
