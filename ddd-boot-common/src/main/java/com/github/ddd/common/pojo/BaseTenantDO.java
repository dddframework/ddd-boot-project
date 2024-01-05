package com.github.ddd.common.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基础实体对象-多租户
 *
 * @author ranger
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseTenantDO extends BaseAuditDO implements Serializable {
    /**
     * 租户id
     */
    private Long tenantId;


}
