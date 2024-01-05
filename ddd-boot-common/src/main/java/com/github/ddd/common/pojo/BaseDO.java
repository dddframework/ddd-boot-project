package com.github.ddd.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体对象
 *
 * @author ranger
 */
@Data
public class BaseDO implements Serializable {

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * /**
     * 最后更新时间
     */
    private Date updateTime;


}
