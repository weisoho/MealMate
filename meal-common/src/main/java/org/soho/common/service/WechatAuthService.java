package org.soho.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soho.common.config.props.ExternalServiceProperties;
import org.soho.common.exception.ApiException;
import org.soho.common.factory.WebClientFactory;
import org.soho.common.mapper.WeChatExceptionMapper;
import org.soho.common.model.bo.WeChatSession;
import org.soho.common.model.bo.WeChatSessionError;
import org.soho.common.model.enums.ErrorCode;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author wesoho
 * @version 1.0
 * @description: TODO
 * @date 2025/4/22 20:04
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WechatAuthService {
    @Resource
    private final WebClientFactory webClientFactory;
    @Resource
    private WeChatExceptionMapper weChatExceptionMapper;

    @Resource
    private final ExternalServiceProperties properties;

    ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 获取微信session_key
     * @param jsCode    入参code
     * @return  微信session_key以及相关信息
     */
    public Mono<WeChatSession> getSession(String jsCode) {
        WebClient client = webClientFactory.getClient("wechat");
        ExternalServiceProperties.ServiceConfig config =
                properties.getServices().get("wechat");

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sns/jscode2session")
                        .queryParam("appid", config.getAppid())
                        .queryParam("secret", config.getSecret())
                        .queryParam("js_code", jsCode)
                        .queryParam("grant_type", "authorization_code")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("微信接口HTTP错误 | 状态码: {} | 响应: {}",
                                            response.statusCode().value(), body);
                                    return Mono.error(new ApiException(
                                            ErrorCode.API_WECHAT_ERROR,
                                            String.valueOf(response.statusCode().value()),
                                            body));
                                }))
                .bodyToMono(String.class)
                .flatMap(body -> {
                    try {
                        // 判断响应中是否包含 errcode 字段
                        if (body.contains("errcode")) {
                            // 失败响应，反序列化为包含错误信息的对象
                            WeChatSessionError error = objectMapper.readValue(body, WeChatSessionError.class);
                            log.error("微信业务错误 | 详细码: {} | 错误信息: {}",
                                    error.getErrcode(), error.getErrmsg());
                            return Mono.error(new ApiException(
                                    weChatExceptionMapper.mapErrorCode(error.getErrcode()),
                                    String.valueOf(error.getErrcode()),
                                    error.getErrmsg()));
                        } else {
                            // 成功响应，反序列化为包含成功信息的对象
                            WeChatSession session = objectMapper.readValue(body, WeChatSession.class);
                            log.info("微信接口返回: {}", session);
                            return Mono.just(session);
                        }
                    } catch (Exception e) {
                        log.error("反序列化微信响应时出错", e);
                        return Mono.error(new ApiException(
                                ErrorCode.API_WECHAT_ERROR,
                                "反序列化微信响应时出错",
                                e.getMessage()));
                    }
                });
    }
}