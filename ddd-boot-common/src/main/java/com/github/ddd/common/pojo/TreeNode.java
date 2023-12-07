package com.github.ddd.common.pojo;

import java.util.List;

/**
 * @author ranger
 */
public interface TreeNode<T, ID> {
    /**
     * 获取ID
     *
     * @return ID
     */
    ID id();

    /**
     * 获取PID
     *
     * @return PID
     */
    ID pid();


    /**
     * 获取名称
     *
     * @return name
     */
    String name();

    /**
     * 子节点
     *
     * @return children
     */
    List<T> children();

    /**
     * 设置子集
     *
     * @param children children
     */
    void fillChildren(List<T> children);
}
