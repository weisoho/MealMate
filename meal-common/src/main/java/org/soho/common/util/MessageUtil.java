package org.soho.common.util;

import org.soho.common.model.enums.ErrorCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author wesoho
 * @version 1.0
 * @description:    系统提示消息工具
 * @date 2024/10/27 11:48
 */
@Component
public class MessageUtil {
    private final MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getMessage(ErrorCode errorCode) {
        return messageSource.getMessage(
                errorCode.getMessage(),
                null,
                LocaleContextHolder.getLocale()
        );
    }
}
