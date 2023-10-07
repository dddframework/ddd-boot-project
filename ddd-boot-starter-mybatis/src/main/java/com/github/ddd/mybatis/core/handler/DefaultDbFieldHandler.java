package com.github.ddd.mybatis.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.ddd.mybatis.core.dataobject.BaseDO;
import com.github.ddd.mybatis.core.dataobject.BaseTenantDO;
import com.github.ddd.security.pojo.UserDetail;
import com.github.ddd.security.util.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;

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
        if (metaObject.getOriginalObject() instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();
            UserDetail user = UserContextHolder.getCurrentUser();
            if (user != null) {
                Long userId = user.getUserId();
                String nickname = user.getNickname();
                baseDO.setCreatorId(userId);
                baseDO.setCreatorName(nickname);
                baseDO.setUpdaterId(userId);
                baseDO.setUpdaterName(nickname);
            } else {
                baseDO.setCreatorId(NOT_LOGIN_ID);
                baseDO.setCreatorName(NOT_LOGIN_NAME);
                baseDO.setUpdaterId(NOT_LOGIN_ID);
                baseDO.setUpdaterName(NOT_LOGIN_NAME);
            }
            if (baseDO.getCreateTime() == null) {
                baseDO.setCreateTime(System.currentTimeMillis());
            }
            if (baseDO.getUpdateTime() == null) {
                baseDO.setUpdateTime(System.currentTimeMillis());
            }
        }
        if (metaObject.getOriginalObject() instanceof BaseTenantDO) {
            BaseTenantDO baseTenantDO = (BaseTenantDO) metaObject.getOriginalObject();
            UserDetail user = UserContextHolder.getCurrentUser();
            if (user != null) {
                Long userId = user.getUserId();
                String nickname = user.getNickname();
                Long tenantId = user.getTenantId();
                baseTenantDO.setCreatorId(userId);
                baseTenantDO.setCreatorName(nickname);
                baseTenantDO.setUpdaterId(userId);
                baseTenantDO.setUpdaterName(nickname);
                baseTenantDO.setTenantId(tenantId);
            } else {
                baseTenantDO.setCreatorId(NOT_LOGIN_ID);
                baseTenantDO.setCreatorName(NOT_LOGIN_NAME);
                baseTenantDO.setUpdaterId(NOT_LOGIN_ID);
                baseTenantDO.setUpdaterName(NOT_LOGIN_NAME);
            }
            if (baseTenantDO.getCreateTime() == null) {
                baseTenantDO.setCreateTime(System.currentTimeMillis());
            }
            if (baseTenantDO.getUpdateTime() == null) {
                baseTenantDO.setUpdateTime(System.currentTimeMillis());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject == null) {
            return;
        }
        UserDetail user = UserContextHolder.getCurrentUser();
        if (metaObject.getOriginalObject() instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();
            if (user != null) {
                Long userId = user.getUserId();
                String nickname = user.getNickname();
                baseDO.setUpdaterId(userId);
                baseDO.setUpdaterName(nickname);
            }
            baseDO.setUpdateTime(System.currentTimeMillis());
        }
        if (metaObject.getOriginalObject() instanceof BaseTenantDO) {
            BaseTenantDO baseDO = (BaseTenantDO) metaObject.getOriginalObject();
            if (user != null) {
                Long userId = user.getUserId();
                String nickname = user.getNickname();
                baseDO.setUpdaterId(userId);
                baseDO.setUpdaterName(nickname);
            }
            baseDO.setUpdateTime(System.currentTimeMillis());
        }

    }
}
