package com.github.ddd.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前端Select Option
 *
 * @author ranger
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectOption {

    /**
     * 文本
     */
    private String label;

    /**
     * 值
     */
    private String value;
}
