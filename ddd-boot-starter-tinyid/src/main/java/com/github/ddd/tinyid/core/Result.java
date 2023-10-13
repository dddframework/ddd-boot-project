package com.github.ddd.tinyid.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author du_imba
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int code;
    private long id;


    /**
     * 正常可用
     */
    public static final int NORMAL = 1;
    /**
     * 需要去加载nextId
     */
    public static final int LOADING = 2;
    /**
     * 超过maxId 不可用
     */
    public static final int OVER = 3;
}
