package com.github.ddd.common.pojo;

import java.util.List;

/**
 * @author 研发中心-ranger
 */
public interface Tree<ID> {
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
     * 权重
     *
     * @return weight
     */
    Integer weight();

    /**
     * 子节点
     *
     * @return children
     */
    List<Tree<ID>> children();

    /**
     * 设置子集
     *
     * @param children children
     */
    void fillChildren(List<Tree<ID>> children);
}
