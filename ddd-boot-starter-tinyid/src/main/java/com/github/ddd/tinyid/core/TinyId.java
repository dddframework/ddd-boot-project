package com.github.ddd.tinyid.core;

import lombok.Data;

/**
 * @author ranger
 */
@Data
public class TinyId {
    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 当前最大id
     */
    private Long maxId;
    /**
     * 步长
     */
    private Integer step;
    /**
     * 每次id增量 默认1
     */
    private Integer delta;
    /**
     * 余数
     */
    private Integer remainder;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
}
