package com.github.ddd.webapp.validation;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.webapp.validation.annotation.ValidDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author ranger
 */
public class DateValidator implements ConstraintValidator<ValidDate, String> {

    private String[] pattern;

    @Override
    public void initialize(final ValidDate constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return true;
        }
        for (String p : pattern) {
            try {
                DateUtil.parse(value, p);
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
