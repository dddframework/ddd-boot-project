package com.github.ddd.common.util.tree;

/**
 * 树节点
 * @author ranger
 * @param <T>
 */
public interface Node<T> {
    /**
     * 节点ID
     * @return
     */
    T getId();

    /**
     * 节点名称
     * @return
     */
    String getNodeName();

    /**
     * 父节点ID
     * @return
     */
    T getPid();
}
