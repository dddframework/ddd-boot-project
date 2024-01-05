package com.github.ddd.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 排序对象
 *
 * @author ranger
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SortingField implements Serializable {

    /**
     * 顺序 - 升序
     */
    public static final String ORDER_ASC = "ASC";
    /**
     * 顺序 - 降序
     */
    public static final String ORDER_DESC = "DESC";

    /**
     * 字段
     */
    private String field;
    /**
     * 顺序
     */
    private String order;
}
