package com.github.ddd.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.ddd.mybatis.core.handler.DefaultDbFieldHandler;
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
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * 自动填充参数类
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler() {
        return new DefaultDbFieldHandler();
    }
}