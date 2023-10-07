package com.github.ddd.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序对象
 *
 * @author ranger
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SortingField {

    /**
     * 顺序 - 升序
     */
    public static final String ORDER_ASC = "asc";
    /**
     * 顺序 - 降序
     */
    public static final String ORDER_DESC = "desc";

    /**
     * 字段
     */
    private String field;
    /**
     * 顺序
     */
    private String order;
}