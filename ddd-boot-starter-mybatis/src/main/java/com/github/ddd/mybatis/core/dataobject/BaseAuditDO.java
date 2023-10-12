package com.github.ddd.mybatis.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基础实体对象
 * 通过实现实现自动填充  {@link com.baomidou.mybatisplus.core.handlers.MetaObjectHandler}
 *
 * @author ranger
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseAuditDO extends BaseDO implements Serializable {

    /**
     * 创建者id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建者姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;
    /**
     * 更新者id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;
    /**
     * 更新者姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updaterName;

}
