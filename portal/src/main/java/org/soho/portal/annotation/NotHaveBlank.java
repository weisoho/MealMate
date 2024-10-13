package org.soho.portal.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.soho.portal.validate.NotHaveBlankValidation;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {NotHaveBlankValidation.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotHaveBlank {
    //验证不通过时默认信息
    String message() default "{org.soho.constraints.NotHaveBlank.message}";

    //属于的分组，默认为default
    Class<?>[] groups() default {};

    //传递额外的信息
    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    //可以有多个@NotBlank注解
    public @interface List {
        NotHaveBlank[] value();
    }
}
