package org.soho.common.exception;

import lombok.Getter;
import org.soho.common.model.enums.ErrorCode;

/**
 * @author wesoho
 * @version 1.0
 * @description: 业务异常
 * @date 2024/10/10 23:04
 */
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
