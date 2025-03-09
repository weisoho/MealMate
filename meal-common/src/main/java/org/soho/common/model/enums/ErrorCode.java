package org.soho.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 用户相关错误
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

    // 系统相关错误
    INTERNAL_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal.error"),
    DATABASE_ERROR(50002, HttpStatus.INTERNAL_SERVER_ERROR.value(), "database.error"),
    NETWORK_ERROR(50003, HttpStatus.INTERNAL_SERVER_ERROR.value(), "network.error");

    // 错误码
    private final int code;
    // http状态码
    private final int httpStatus;
    // 消息键（用于资源文件）
    private final String messageKey;

    // 静态成员变量用于保存MessageSource
    private static MessageSource messageSource;

    /**
     * 设置MessageSource，通常在应用程序启动时调用
     */
    public static void setMessageSource(MessageSource messageSource) {
        ErrorCode.messageSource = messageSource;
    }

    /**
     * 获取具体的错误信息
     */
    public String getMessage() {
        if (messageSource != null) {
            return messageSource.getMessage(this.messageKey, null, LocaleContextHolder.getLocale());
        }
        return this.messageKey; // 如果没有设置MessageSource，则返回默认的消息键
    }
}