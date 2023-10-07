package com.github.ddd.common.pojo;

import java.util.List;

/**
 * @author 研发中心-彭幸园
 */
public interface Tree<ID> {
    /**
     * 获取ID
     *
     * @return
     */
    ID id();

    /**
     * 获取PID
     *
     * @return
     */
    ID pid();


    /**
     * 获取名称
     *
     * @return
     */
    String name();

    /**
     * 权重
     *
     * @return
     */
    Integer weight();

    /**
     * 子节点
     *
     * @return
     */
    List<Tree<ID>> children();

    /**
     * 设置子集
     *
     * @param children
     */
    void fillChildren(List<Tree<ID>> children);
}
