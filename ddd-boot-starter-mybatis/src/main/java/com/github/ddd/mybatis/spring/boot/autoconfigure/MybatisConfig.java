package com.github.ddd.mybatis.spring.boot.autoconfigure;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.ddd.common.util.UserContextHolder;
import com.github.ddd.mybatis.core.handler.DefaultDbFieldHandler;
import com.github.ddd.mybatis.core.exception.DaoExceptionHandler;
import com.github.ddd.mybatis.core.tenant.TenantDbHandler;
import com.github.ddd.mybatis.core.tinyid.SegmentIdService;
import com.github.ddd.mybatis.core.tinyid.TinyIdGeneratorFactory;
import com.github.ddd.mybatis.core.weblog.LogAdvice;
import com.github.ddd.mybatis.core.weblog.LogAdvisor;
import com.github.ddd.mybatis.core.weblog.LogPointCut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * MyBaits 配置类
 *
 * @author ranger
 */
@RequiredArgsConstructor
@Configuration
@Order(99)
@Slf4j
@EnableConfigurationProperties({MybatisProperties.class})
public class MybatisConfig {


    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MybatisProperties mybatisProperties) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 启用多租户模式
        if (mybatisProperties.isEnableSaas()) {
            String prefix = mybatisProperties.getSchemaPrefix();
            if (StrUtil.isBlank(prefix)) {
                throw new RuntimeException("多租户模式 前缀不能为空");
            }
            DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
            dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
                Long tenantId = UserContextHolder.getCurrentUser().getTenantId();
                if (StrUtil.contains(tableName,".")){
                    return tableName;
                }
                //`prefix`.`tableName`
                return StrUtil.format("`{}{}`.`{}`", prefix, tenantId, tableName);
            });
            mybatisPlusInterceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
            log.info("mybatis 启用多租户模式 租户数据库前缀 {}", prefix);
        } else {
            log.info("mybatis 启用单体模式");
        }
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    public TenantDbHandler tenantDbHandler(MybatisProperties mybatisProperties){
        return new TenantDbHandler(mybatisProperties);
    }


    @Bean
    public SegmentIdService segmentIdService(JdbcTemplate jdbcTemplate, MybatisProperties tinyIdProperties, TenantDbHandler tenantDbHandler) {
        return new SegmentIdService(jdbcTemplate, tinyIdProperties, tenantDbHandler);
    }

    @Bean
    public TinyIdGeneratorFactory tinyIdGeneratorFactory(SegmentIdService segmentIdService) {
        return new TinyIdGeneratorFactory(segmentIdService);
    }

    /**
     * 自动填充参数类
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler() {
        return new DefaultDbFieldHandler();
    }


    @Bean
    public LogAdvisor init(JdbcTemplate jdbcTemplate, MybatisProperties webLogProperties, TenantDbHandler tenantDbHandler) {
        LogAdvisor logAdvisor = new LogAdvisor();
        logAdvisor.setLogPointCut(new LogPointCut());
        logAdvisor.setAdvice(new LogAdvice(jdbcTemplate, webLogProperties, tenantDbHandler));
        return logAdvisor;
    }

    /**
     * 异常处理
     */
    @Bean
    public DaoExceptionHandler daoExceptionHandler() {
        return new DaoExceptionHandler();
    }
}
