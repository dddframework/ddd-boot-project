package com.github.ddd.common.util.tree;

import lombok.Data;

import java.util.List;

/**
 * 树
 * @author ranger
 */
@Data
public class Tree<T> {
    /**
     * 跟节点
     */
    private Node<T> root;
    /**
     * 子节点
     */
    private List<Node<T>> children;
}
