package com.github.ddd.web.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.web.validation.annotation.ValidPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author ranger
 */
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (StrUtil.isNotBlank(value)) {
            return Validator.isMobile(value);
        }
        return true;
    }
}
