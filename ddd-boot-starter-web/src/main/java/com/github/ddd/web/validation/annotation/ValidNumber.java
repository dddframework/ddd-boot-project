package com.github.ddd.web.validation.annotation;


import com.github.ddd.web.validation.NumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数字 校验
 *
 * @author ranger
 */
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = NumberValidator.class)
@Documented
public @interface ValidNumber {

    String message() default "数字不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 有效位
     *
     * @return
     */
    int p();

    /**
     * 小数点后的位数
     *
     * @return
     */
    int d();
}
