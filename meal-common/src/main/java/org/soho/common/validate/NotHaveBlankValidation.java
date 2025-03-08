package org.soho.common.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.soho.common.annotation.NotHaveBlank;

/**
 * @author wesoho
 * @version 1.0
 * @description: 字符串不能包含空值入参校验
 * @date 2024/10/13 10:30
 */
public class NotHaveBlankValidation implements ConstraintValidator<NotHaveBlank,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(value)) {
            return false;
        }

        return !value.contains(" ");
    }
}
