package org.soho.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 用户相关异常
    NOT_LOGIN(10000,HttpStatus.UNAUTHORIZED.value(),"user.not.login"),
    USER_NOT_FOUND(10001, HttpStatus.NOT_FOUND.value(), "user.not.found"),
    INVALID_INPUT(10002, HttpStatus.BAD_REQUEST.value(), "invalid.input"),
    DUPLICATE_EMAIL(10003, HttpStatus.BAD_REQUEST.value(), "duplicate.email"),
    DUPLICATE_PHONE_NUMBER(10004, HttpStatus.BAD_REQUEST.value(), "duplicate.phone.number"),
    PHONE_OR_EMAIL_REQUIRED(10005, HttpStatus.BAD_REQUEST.value(), "phone.or.email.required"),
    INVALID_PASSWORD(10006, HttpStatus.BAD_REQUEST.value(), "invalid.password"),
    LOGIN_FAILED(10007, 409, "login.failed"),
    ARTICLE_TITLE_NOT_NULL(10008, 409, "article.title.not.null"),
    FILE_NOT_FOUND(10009, 409, "file.not.found"),
    USER_CREATE_FAILED(10010, 409, "user.create.failed"),

    // 系统相关异常
    INTERNAL_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal.error"),
    DATABASE_ERROR(50002, HttpStatus.INTERNAL_SERVER_ERROR.value(), "database.error"),
    NETWORK_ERROR(50003, HttpStatus.INTERNAL_SERVER_ERROR.value(), "network.error"),

    // API相关异常
    API_WECHAT_ERROR(40001, HttpStatus.INTERNAL_SERVER_ERROR.value(), "api.wechat.error"),
    API_WECHAT_INVALID_CODE(40002, HttpStatus.INTERNAL_SERVER_ERROR.value(), "api.wechat.error"),
    API_WECHAT_RATE_LIMIT(40003, HttpStatus.INTERNAL_SERVER_ERROR.value(), "api.wechat.error"),
    API_WECHAT_BLOCKED(40004, HttpStatus.INTERNAL_SERVER_ERROR.value(), "api.wechat.error");

    // 错误码
    private final int code;
    // http状态码
    private final int httpStatus;
    // 消息键（用于资源文件）
    private final String message;
}