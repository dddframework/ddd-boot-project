package com.github.ddd.validation.util;


import com.github.ddd.validation.annotation.*;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

class ValidationUtilTest {

    @Data
    public static class Entry {
        @ValidChinese
        private String field_1;
        @ValidDate
        private String field_2;
        @ValidEmail
        private String field_3;
        @ValidEnum(option = {"a", "b"})
        private String field_4;
        @ValidIdCard
        private String field_5;
        @ValidNumber(p = 3, d = 2)
        private String field_6;
        @ValidUrl
        private String field_7;
    }

    @Test
    void validate() {
        Entry entry = new Entry();
        entry.setField_1("A");
        entry.setField_2("2");
        entry.setField_3("3");
        entry.setField_4("c");
        entry.setField_5("1111");
        entry.setField_6("5355.489");
        entry.setField_7("asdasd");
        BeanValidationResult result = ValidationUtil.warpValidate(entry);
        List<BeanValidationResult.ErrorMessage> errorMessages = result.getErrorMessages();
        for (BeanValidationResult.ErrorMessage errorMessage : errorMessages) {
            System.out.println(errorMessage.getMessage() + "==" + errorMessage.getValue());
        }

        System.out.println("===========================================");

        Entry entry1 = new Entry();
        entry1.setField_1("中文");
        entry1.setField_2("2020/7/8");
        entry1.setField_3("928754557@qq.com");
        entry1.setField_4("a");
        entry1.setField_5("110101199003076878");
        entry1.setField_6("000999.990000");
        entry1.setField_7("https://www.baidu.com");
        BeanValidationResult result1 = ValidationUtil.warpValidate(entry1);
        List<BeanValidationResult.ErrorMessage> errorMessages1 = result1.getErrorMessages();
        for (BeanValidationResult.ErrorMessage errorMessage : errorMessages1) {
            System.out.println(errorMessage.getMessage() + "==" + errorMessage.getValue());
        }
    }
}