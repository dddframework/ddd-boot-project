package com.github.ddd.mybatis.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.ddd.mybatis.core.handler.DefaultDbFieldHandler;
import com.github.ddd.mybatis.exception.DaoExceptionHandler;
import com.github.ddd.common.util.UserContextHolder;
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
@RequiredArgsConstructor
@EnableConfigurationProperties(TenantProperties.class)
@Configuration
@Order(99)
public class MybatisConfig {

    private final TenantProperties tenantProperties;

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 启用多租户模式
        if (tenantProperties.isEnable()) {
            String prefix = tenantProperties.getSchemaPrefix();
            if (StrUtil.isBlank(prefix)){
                throw new RuntimeException("多租户模式 前缀不能为空");
            }
            DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
            dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
                Long tenantId = UserContextHolder.getCurrentUser().getTenantId();
                //`prefix`.`tableName`
                return StrUtil.format("`{}{}`.`{}`", prefix, tenantId, tableName);
            });
            mybatisPlusInterceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        }
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

    /**
     * 异常处理
     */
    @Bean
    public DaoExceptionHandler daoExceptionHandler() {
        return new DaoExceptionHandler();
    }
}
