package com.github.ddd.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.validation.annotation.ValidMobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author ranger
 */
public class MobileValidator implements ConstraintValidator<ValidMobile, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (StrUtil.isNotBlank(value)) {
            return Validator.isMobile(value);
        }
        return true;
    }
}
