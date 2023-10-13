package com.github.ddd.security.spring.boot.autoconfigure;

import cn.hutool.core.collection.CollUtil;
import com.github.ddd.security.core.SessionManager;
import com.github.ddd.security.filter.PermissionInterceptor;
import com.github.ddd.security.filter.UserContextFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableCaching
@ConditionalOnClass(CacheManager.class)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;


    @Bean
    public SessionManager sessionManager(CacheManager cacheManager) {
        return new SessionManager(cacheManager, securityProperties);
    }

    @Bean
    public UserContextFilter userContextFilter(SessionManager sessionManager) {
        return new UserContextFilter(sessionManager);
    }

    @Bean
    @ConditionalOnMissingBean
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
