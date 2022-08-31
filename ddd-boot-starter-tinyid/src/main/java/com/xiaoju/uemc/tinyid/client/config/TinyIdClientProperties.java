package com.xiaoju.uemc.tinyid.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author du_imba
 */
@Data
@ConfigurationProperties(prefix = "tinyid")
public class TinyIdClientProperties {

    private String tinyIdToken;
    private String tinyIdServer;
    private Integer readTimeout;
    private Integer connectTimeout;
}
