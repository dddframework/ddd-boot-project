package com.github.ddd.webapp.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ranger
 */
@Configuration
@EnableConfigurationProperties(SpringDocProperties.class)
public class SpringDocConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("DDD API")
                        .description("SpringDoc API 演示")
                        .version("v1.0.0")
                        .license(new License().name("MIT").url("https://gitee.com/pengxingyuan/ddd")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringBoot DDD 领域驱动设计 后台框架")
                        .url("https://gitee.com/pengxingyuan"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1.0")
                .pathsToMatch("/**")
                .build();
    }
}
