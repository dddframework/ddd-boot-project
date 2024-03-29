package com.github.ddd.validation.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 封装校验工具
 *
 * @author ranger
 */
public class ValidationUtil {

    private static final Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 获取原生{@link Validator} 对象
     *
     * @return {@link Validator} 对象
     */
    public static Validator getValidator() {
        return VALIDATOR;
    }

    /**
     * 校验对象
     *
     * @param <T>    Bean类型
     * @param bean   bean
     * @param groups 校验组
     * @return {@link Set}
     */
    public static <T> Set<ConstraintViolation<T>> validate(T bean, Class<?>... groups) {
        return VALIDATOR.validate(bean, groups);
    }


    /**
     * 校验对象
     *
     * @param <T>    Bean类型
     * @param bean   bean
     * @param groups 校验组
     * @return {@link BeanValidationResult}
     */
    public static <T> BeanValidationResult warpValidate(T bean, Class<?>... groups) {
        return warpBeanValidationResult(validate(bean, groups));
    }


    /**
     * 包装校验结果
     *
     * @param constraintViolations 校验结果集
     * @return {@link BeanValidationResult}
     */
    private static <T> BeanValidationResult warpBeanValidationResult(Set<ConstraintViolation<T>> constraintViolations) {
        BeanValidationResult result = new BeanValidationResult(constraintViolations.isEmpty());
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            BeanValidationResult.ErrorMessage errorMessage = new BeanValidationResult.ErrorMessage();
            errorMessage.setPropertyName(constraintViolation.getPropertyPath().toString());
            errorMessage.setMessage(constraintViolation.getMessage());
            errorMessage.setValue(constraintViolation.getInvalidValue());
            result.addErrorMessage(errorMessage);
        }
        return result;
    }
}
