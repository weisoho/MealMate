package org.soho.common.config;

import lombok.extern.slf4j.Slf4j;
import org.soho.common.config.props.ExternalServiceProperties;
import org.soho.common.factory.WebClientFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author wesoho
 * @version 1.0
 * @description: WebClient配置类
 * @date 2025/4/21 0:48
 */
// src/main/java/com/example/config/WebClientConfig.java
@Configuration
@EnableConfigurationProperties(ExternalServiceProperties.class)
@Slf4j
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(logRequest())
                .filter(logResponse())
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(config -> config.defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build());
    }

    @Bean
    public WebClientFactory webClientFactory(
            WebClient.Builder baseBuilder,
            ExternalServiceProperties properties) {
        return new WebClientFactory(baseBuilder, properties.getServices());
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) ->
                    values.forEach(value -> log.info("Header: {}={}", name, value))
            );
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info("Response status: {}", response.statusCode());
            return Mono.just(response);
        });
    }
}