package com.github.ddd.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.validation.annotation.ValidNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * @author ranger
 */
public class NumberValidator implements ConstraintValidator<ValidNumber, String> {

    private double p;
    private double d;

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
            int precision = decimal.precision();
            int scale = decimal.scale();
            if (precision - scale > p) {
                return false;
            }
            return scale <= d;
        }
        return false;
    }
}
