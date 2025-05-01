package org.soho.common.mapper;

import org.soho.common.model.enums.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wesoho
 * @version 1.0
 * @description: 微信获取session错误码映射器
 * @date 2025/4/26 0:21
 */
@Component
public class WeChatExceptionMapper {
    private static final Map<Integer, ErrorCode> ERROR_MAPPING = Map.of(
            40029, ErrorCode.API_WECHAT_INVALID_CODE,
            45011, ErrorCode.API_WECHAT_RATE_LIMIT,
            40226, ErrorCode.API_WECHAT_BLOCKED
    );

    public ErrorCode mapErrorCode(int wechatErrorCode) {
        return ERROR_MAPPING.getOrDefault(wechatErrorCode, ErrorCode.API_WECHAT_ERROR);
    }
}
