package com.xiaoju.uemc.tinyid.client.config;

import com.xiaoju.uemc.tinyid.client.factory.impl.IdGeneratorFactoryClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ranger
 */
@Configuration
@EnableConfigurationProperties(TinyIdClientProperties.class)
public class TinyIdClientConfig {

    @Bean
    public IdGeneratorFactoryClient idGeneratorFactoryClient(TinyIdClientProperties properties) {
        return new IdGeneratorFactoryClient(properties);
    }
}
