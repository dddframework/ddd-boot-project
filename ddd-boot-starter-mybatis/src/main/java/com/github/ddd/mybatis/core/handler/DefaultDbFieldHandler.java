package com.github.ddd.mybatis.core.handler;

/**
 * @author ranger
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.ddd.mybatis.core.dataobject.BaseDO;
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
                String username = user.getUsername();
                baseDO.setCreatorId(userId);
                baseDO.setCreatorName(username);
                baseDO.setUpdaterId(userId);
                baseDO.setUpdaterName(username);
            } else {
                baseDO.setCreatorId(-1L);
                baseDO.setCreatorName("匿名用户");
                baseDO.setUpdaterId(-1L);
                baseDO.setUpdaterName("匿名用户");
            }
            baseDO.setCreateTime(System.currentTimeMillis());
            baseDO.setUpdateTime(System.currentTimeMillis());
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
                String username = user.getUsername();
                baseDO.setUpdaterName(username);
                baseDO.setUpdateTime(System.currentTimeMillis());
            } else {
                baseDO.setCreatorId(-1L);
                baseDO.setCreatorName("匿名用户");
                baseDO.setUpdaterId(-1L);
                baseDO.setUpdaterName("匿名用户");
            }
        }

    }
}
