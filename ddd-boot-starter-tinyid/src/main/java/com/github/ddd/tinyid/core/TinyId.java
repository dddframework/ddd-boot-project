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
    private Long step;
    /**
     * 每次id增量 默认1
     */
    private Long delta;
    /**
     * 余数
     */
    private Long remainder;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
}
