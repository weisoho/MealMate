package org.soho.common.exception;

import lombok.Getter;
import org.soho.common.model.enums.ErrorCode;

/**
 * @author wesoho
 * @version 1.0
 * @description: 调用API异常
 * @date 2025/4/26 0:03
 */
@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detailCode; // 新增微信错误码字段
    private final String detailMessage; // 新增微信错误描述字段

    public ApiException(ErrorCode errorCode, String detailCode, String detailMessage) {
        super(errorCode.getMessage() + " [Detail: " + detailCode + " - " + detailMessage + "]");
        this.errorCode = errorCode;
        this.detailCode = detailCode;
        this.detailMessage = detailMessage;
    }

}