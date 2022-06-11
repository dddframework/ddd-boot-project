package com.github.ddd.database.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.ddd.database.exception.DaoExceptionHandler;
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
public class MybatisAutoConfiguration {

    /**
     * 分页插件
     *
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
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