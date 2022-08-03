package com.github.ddd.validation;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.validation.annotation.ValidEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author ranger
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private List<String> option;

    @Override
    public void initialize(final ValidEnum constraintAnnotation) {
        this.option = ListUtil.of(constraintAnnotation.option());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value) || option.contains(value)) {
            return true;
        }

        return false;
    }
}
