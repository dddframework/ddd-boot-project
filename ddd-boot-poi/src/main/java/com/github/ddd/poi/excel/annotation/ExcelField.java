package com.github.ddd.poi.excel.annotation;

import org.apache.poi.ss.usermodel.BuiltinFormats;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ranger
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    /**
     * 列名
     *
     * @return
     */
    String name();

    /**
     * 导入解析日期格式, 如果不指定 有些数字会被解析成1970年的时间
     *
     * @return
     */
    String dateTimeFormat() default "";

    /**
     * 导出数据格式 时间类型不填默认使用dateTimeFormat的值
     * set the data format (must be a valid format). Built in formats are defined at {@link BuiltinFormats}.
     * 百分比 0.00%
     * 时间 yyyy/MM/dd
     * 金额-人民币 "$"#,##0_);[Red]("$"#,##0)
     */
    String dataFormat() default "";
}
