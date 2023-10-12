package com.github.ddd.mybatis.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基础实体对象-多租户
 * 通过实现实现自动填充  {@link com.baomidou.mybatisplus.core.handlers.MetaObjectHandler}
 *
 * @author ranger
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseTenantDO extends BaseAuditDO implements Serializable {
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long tenantId;


}
