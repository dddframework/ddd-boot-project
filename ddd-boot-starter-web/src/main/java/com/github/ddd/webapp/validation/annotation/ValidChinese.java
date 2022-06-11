package com.github.ddd.webapp.validation.annotation;


import com.github.ddd.webapp.validation.ChineseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 中文 校验
 *
 * @author ranger
 */
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ChineseValidator.class)
@Documented
public @interface ValidChinese {

    String message() default "必须是中文";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
