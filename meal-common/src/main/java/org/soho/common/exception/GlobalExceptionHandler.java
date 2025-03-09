package org.soho.common.exception;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.soho.common.model.enums.ErrorCode;
import org.soho.common.model.vo.BaseResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
        log.error("Handling MethodArgumentNotValidException:{}", e.getMessage());
        String badRequestMsg = messageSource.getMessage("exception.badRequest", null, LocaleContextHolder.getLocale());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), badRequestMsg);
        }
        // 自定义注解写的Message信息
        String defaultErrorMessage = fieldErrors.get(0).getDefaultMessage();

        return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), badRequestMsg, defaultErrorMessage);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Object>> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException{}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        BaseResponse<Object> error = BaseResponse.error(errorCode.getCode(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(errorCode.getHttpStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> exceptionHandler(Exception e) {
        log.error("Exception{}", e.getMessage());
        BaseResponse<Object> error = BaseResponse.error(ErrorCode.INTERNAL_ERROR, e.getMessage());
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(ErrorCode.INTERNAL_ERROR.getHttpStatus()));
    }
}
