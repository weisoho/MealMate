package org.soho.portal.exception;

import lombok.extern.slf4j.Slf4j;
import org.soho.portal.common.BaseResponse;
import org.soho.portal.model.enums.ErrorCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author wesoho
 * @version 1.0
 * @description: 全局异常处理器
 * @date 2024/10/11 0:20
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<String> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return BaseResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<String> exceptionHandler(Exception e) {
        log.error("BusinessException", e);
        return BaseResponse.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
