package com.github.ddd.mybatis.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.ddd.common.pojo.UserDetail;
import com.github.ddd.common.util.UserContextHolder;
import com.github.ddd.mybatis.core.dataobject.BaseAuditDO;
import com.github.ddd.mybatis.core.dataobject.BaseDO;
import com.github.ddd.mybatis.core.dataobject.BaseTenantDO;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 通用参数填充实现类
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author ranger
 */
public class DefaultDbFieldHandler implements MetaObjectHandler {

    private static final Long NOT_LOGIN_ID = -1L;
    private static final String NOT_LOGIN_NAME = "未登录用户";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject == null) {
            return;
        }
        Date date = new Date();
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) originalObject;
            if (baseDO.getCreateTime() == null) {
                baseDO.setCreateTime(date);
            }
            if (baseDO.getUpdateTime() == null) {
                baseDO.setUpdateTime(date);
            }
        }
        if (originalObject instanceof BaseAuditDO) {
            BaseAuditDO baseAuditDO = (BaseAuditDO) originalObject;
            UserDetail user = UserContextHolder.getCurrentUser();
            if (user != null) {
                Long userId = user.getUserId();
                String nickname = user.getNickname();
                baseAuditDO.setCreatorId(userId);
                baseAuditDO.setCreatorName(nickname);
                baseAuditDO.setUpdaterId(userId);
                baseAuditDO.setUpdaterName(nickname);
            } else {
                baseAuditDO.setCreatorId(NOT_LOGIN_ID);
                baseAuditDO.setCreatorName(NOT_LOGIN_NAME);
                baseAuditDO.setUpdaterId(NOT_LOGIN_ID);
                baseAuditDO.setUpdaterName(NOT_LOGIN_NAME);
            }
        }
        if (originalObject instanceof BaseTenantDO) {
            BaseTenantDO baseTenantDO = (BaseTenantDO) originalObject;
            UserDetail user = UserContextHolder.getCurrentUser();
            if (user != null) {
                Long tenantId = user.getTenantId();
                baseTenantDO.setTenantId(tenantId);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject == null) {
            return;
        }
        Date date = new Date();
        UserDetail user = UserContextHolder.getCurrentUser();
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) originalObject;
            if (baseDO.getUpdateTime() == null) {
                baseDO.setUpdateTime(date);
            }
        }
        if (originalObject instanceof BaseAuditDO) {
            BaseAuditDO baseAuditDO = (BaseAuditDO) originalObject;
            if (user != null) {
                Long userId = user.getUserId();
                String nickname = user.getNickname();
                baseAuditDO.setUpdaterId(userId);
                baseAuditDO.setUpdaterName(nickname);
            }
        }
    }

}
