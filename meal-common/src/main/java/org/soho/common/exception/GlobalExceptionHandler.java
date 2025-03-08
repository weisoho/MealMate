package org.soho.common.exception;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.soho.common.model.enums.ErrorCode;
import org.soho.common.model.vo.BaseResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author wesoho
 * @version 1.0
 * @description: 全局异常处理器
 * @date 2024/10/11 0:20
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Resource
    private MessageSource messageSource;

    // 处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("Handling MethodArgumentNotValidException:{}",e.getMessage());
        String badRequestMsg = messageSource.getMessage("exception.badRequest", null, LocaleContextHolder.getLocale());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), badRequestMsg);
        }
        //自定义注解写的Message信息
        String defaultErrorMessage = fieldErrors.get(0).getDefaultMessage();

        return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), badRequestMsg, defaultErrorMessage);
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<String> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException{}", e.getMessage());
        return BaseResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<String> exceptionHandler(Exception e) {
        log.error("Exception{}", e.getMessage());
        return BaseResponse.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
