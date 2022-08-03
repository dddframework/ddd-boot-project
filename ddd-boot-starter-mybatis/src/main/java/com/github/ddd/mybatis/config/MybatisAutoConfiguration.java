package com.github.ddd.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.ddd.mybatis.exception.DaoExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * MyBaits 配置类
 *
 * @author ranger
 */
@Configuration
@Order(99)
@EnableConfigurationProperties(SaasDbProperties.class)
@RequiredArgsConstructor
public class MybatisAutoConfiguration {


    private final SaasDbProperties saasDbProperties;

    /**
     * 分页插件
     *
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        if (saasDbProperties.isEnable()) {
            String prefix = saasDbProperties.getPrefix();
            DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
            dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
                Long tenantId = 1L;
                return String.format("`%_%`.`%`", prefix, tenantId, tableName);
            });
            mybatisPlusInterceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        }
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * DAO层异常处理
     * @return
     */
    @Bean
    public DaoExceptionHandler daoExceptionHandler() {
        return new DaoExceptionHandler();
    }
}