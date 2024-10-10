package org.soho.portal.exception;

import lombok.Getter;
import org.soho.portal.model.enums.ErrorCode;

/**
 * @author wesoho
 * @version 1.0
 * @description: 业务异常
 * @date 2024/10/10 23:04
 */
@Getter
public class BusinessException extends RuntimeException{
    //错误码
    private final int code;

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode,String msg) {
        super(msg);
        this.code = errorCode.getCode();
    }
}
