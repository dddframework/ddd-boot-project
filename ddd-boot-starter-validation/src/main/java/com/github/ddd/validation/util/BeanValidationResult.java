package com.github.ddd.validation.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * bean 校验结果
 *
 * @author chengqiang
 * @since 5.5.0
 */
@Data
public class BeanValidationResult {
    /**
     * 校验是否成功
     */
    private boolean success;
    /**
     * 错误消息
     */
    private List<ErrorMessage> errorMessages = new ArrayList<>();

    /**
     * 构造
     *
     * @param success 是否验证成功
     */
    public BeanValidationResult(boolean success) {
        this.success = success;
    }


    /**
     * 增加错误信息
     *
     * @param errorMessage 错误信息
     */
    public void addErrorMessage(ErrorMessage errorMessage) {
        this.errorMessages.add(errorMessage);
    }

    /**
     * 错误消息，包括字段名（字段路径）、消息内容和字段值
     */
    @Data
    public static class ErrorMessage {
        /**
         * 属性字段名称
         */
        private String propertyName;
        /**
         * 错误信息
         */
        private String message;
        /**
         * 错误值
         */
        private Object value;
    }
}
