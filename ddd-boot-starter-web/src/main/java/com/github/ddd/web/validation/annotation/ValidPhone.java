package com.github.ddd.web.validation.annotation;


import com.github.ddd.web.validation.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 手机号 校验
 *
 * @author ranger
 */
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface ValidPhone {

    String message() default "电话号码不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
