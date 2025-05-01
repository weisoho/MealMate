package org.soho.common.factory;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import org.soho.common.config.props.ExternalServiceProperties;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

/**
 * @author wesoho
 * @version 1.0
 * @description: WebClientFactory 工厂类
 * @date 2025/4/21 22:26
 */
@Component
public class WebClientFactory {
    private final WebClient.Builder baseBuilder;
    private final Map<String, ExternalServiceProperties.ServiceConfig> services;

    public WebClientFactory(
            WebClient.Builder baseBuilder,
            Map<String, ExternalServiceProperties.ServiceConfig> services) {
        this.baseBuilder = baseBuilder;
        this.services = services;
        validateAllConfigs();
    }

    public WebClient getClient(String serviceName) {
        ExternalServiceProperties.ServiceConfig config = services.get(serviceName);
        validateConfig(serviceName, config);
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        (int) config.getConnectTimeout().toMillis())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(
                                (int) config.getReadTimeout().getSeconds()))
                        .addHandlerLast(new WriteTimeoutHandler(
                                (int) config.getWriteTimeout().getSeconds())));

        return baseBuilder.clone()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(config.getBaseUrl())
                .defaultHeaders(headers ->
                        config.getDefaultHeaders().forEach(headers::add))
                .build();
    }

    @PostConstruct
    private void validateAllConfigs() {
        services.forEach((name, config) -> {
            Assert.notNull(config, "Service " + name + " not configured");
            Assert.hasText(config.getBaseUrl(),
                    "Service " + name + " missing baseUrl");
        });
    }

    private void validateConfig(String serviceName,
                                ExternalServiceProperties.ServiceConfig config) {
        Assert.notNull(config, "Service not found: " + serviceName);
        Assert.hasText(config.getBaseUrl(), "baseUrl required for " + serviceName);
    }
}