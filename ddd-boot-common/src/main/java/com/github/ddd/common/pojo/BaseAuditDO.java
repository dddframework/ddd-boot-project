package com.github.ddd.common.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基础实体对象
 *
 * @author ranger
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseAuditDO extends BaseDO implements Serializable {

    /**
     * 创建者id
     */
    private Long creatorId;
    /**
     * 创建者姓名
     */
    private String creatorName;
    /**
     * 更新者id
     */
    private Long updaterId;
    /**
     * 更新者姓名
     */
    private String updaterName;

}
