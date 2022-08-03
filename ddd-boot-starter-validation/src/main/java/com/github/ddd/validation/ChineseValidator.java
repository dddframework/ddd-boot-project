package com.github.ddd.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.validation.annotation.ValidChinese;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author ranger
 */
public class ChineseValidator implements ConstraintValidator<ValidChinese, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (StrUtil.isNotBlank(value)) {
            return Validator.isChinese(value);
        }
        return true;
    }
}