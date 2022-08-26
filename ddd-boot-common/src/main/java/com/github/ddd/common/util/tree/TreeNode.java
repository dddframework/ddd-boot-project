package com.github.ddd.common.util.tree;

import java.util.List;

/**
 * 树节点
 *
 * @param <T>
 * @author ranger
 */
public interface TreeNode<T> {
    /**
     * 节点ID
     *
     * @return
     */
    T getId();

    /**
     * 节点名称
     *
     * @return
     */
    String getNodeName();

    /**
     * 父节点ID
     *
     * @return
     */
    T getPid();

    List<? extends TreeNode> getChildren();


    void setChildren(List<TreeNode<T>> treeNodes);
}
