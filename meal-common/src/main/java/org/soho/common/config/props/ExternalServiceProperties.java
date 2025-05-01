package org.soho.common.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wesoho
 * @version 1.0
 * @description: TODO
 * @date 2025/4/21 22:22
 */
@ConfigurationProperties(prefix = "external-services")
@Getter
@Setter
public class ExternalServiceProperties {
    private Map<String, ServiceConfig> services = new HashMap<>();

    @Getter
    @Setter
    public static class ServiceConfig {
        private String baseUrl;
        private String appid;
        private String secret;
        private Map<String, String> defaultHeaders = new HashMap<>();
        private Duration connectTimeout = Duration.ofSeconds(5);
        private Duration readTimeout = Duration.ofSeconds(10);
        private Duration writeTimeout = Duration.ofSeconds(10);
    }
}