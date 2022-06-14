package com.github.ddd.web.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 封装校验工具
 *
 * @author ranger
 */
public class ValidationUtil {

    public static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验实体参数
     *
     * @param t bean
     * @return errors List
     */
    public static <T> List<String> validate(T t) {
        assert t != null;
        Set<ConstraintViolation<T>> validationSet = VALIDATOR.validate(t, Default.class);
        List<String> errors = new ArrayList<>();
        if (validationSet != null && validationSet.size() > 0) {
            ConstraintViolation<T> violation = validationSet.iterator().next();
            errors.add(violation.getMessage());
        }
        return errors;
    }
}
