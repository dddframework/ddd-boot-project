package com.github.ddd.web.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.web.validation.annotation.ValidNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * @author ranger
 */
public class NumberValidator implements ConstraintValidator<ValidNumber, String> {

    private int p;
    private int d;

    @Override
    public void initialize(final ValidNumber constraintAnnotation) {
        this.p = constraintAnnotation.p();
        this.d = constraintAnnotation.d();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return true;
        }
        boolean b = Validator.isNumber(value);
        if (b) {
            BigDecimal decimal = new BigDecimal(value).stripTrailingZeros();
            if (decimal.precision() > p) {
                return false;
            }
            return decimal.scale() <= d;
        }
        return false;
    }
}
