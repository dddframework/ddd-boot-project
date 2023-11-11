package com.github.ddd.common.util;

import cn.hutool.core.collection.CollUtil;
import com.github.ddd.common.exception.ClientException;
import com.github.ddd.common.exception.SystemException;
import com.github.ddd.common.pojo.TreeNode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author ranger
 */
@Slf4j
public class TreeBuilder<T extends TreeNode<T, ID>, ID> {
    /**
     * 最大递归深度
     */
    private int maxDeep = 15;

    /**
     * 节点之间的父子关系
     */
    private final Map<ID, List<T>> pTreeMap = new HashMap<>();

    public TreeBuilder(Collection<T> nodeList) {
        if (CollUtil.isEmpty(nodeList)) {
            throw new ClientException("数据集不能为空");
        }
        // 构建父子关系
        for (T data : nodeList) {
            ID pid = data.pid();
            List<T> trees = pTreeMap.get(pid);
            if (trees == null) {
                List<T> value = new ArrayList<>();
                value.add(data);
                pTreeMap.put(pid, value);
            } else {
                trees.add(data);
                pTreeMap.put(pid, trees);
            }
        }
    }

    public TreeBuilder(List<T> nodeList, int maxDeep) {
        this(nodeList);
        this.maxDeep = maxDeep;
    }

    /**
     * 递归处理
     *
     * @param tree     tree
     * @param pTreeMap pTreeMap
     * @param deep     deep
     */
    private void buildTree(T tree, Map<ID, List<T>> pTreeMap, int deep) {
        ID id = tree.id();
        log.debug("当前节点 {} 递归深度 {}", id, deep);
        if (deep > maxDeep) {
            throw new SystemException("数据不合法，超出最大递归深度" + maxDeep + " 该节点标识是" + id);
        }
        List<T> childrenTree = pTreeMap.get(id);
        if (childrenTree != null) {
            tree.fillChildren(childrenTree);
            for (T item : childrenTree) {
                buildTree(item, pTreeMap, deep + 1);
            }
        }
    }

    /**
     * 构建树
     *
     * @param topId 顶级节点
     * @param sort  是否排序
     * @return List<T>
     */
    public List<T> buildTree(ID topId, boolean sort) {
        List<T> trees = pTreeMap.get(topId);
        if (trees == null) {
            return new ArrayList<>();
        }
        for (T tree : trees) {
            buildTree(tree, pTreeMap, 1);
        }
        if (sort) {
            trees.sort(Comparator.comparing(TreeNode::weight));
            for (T tree : trees) {
                sortTree(tree);
            }
        }
        return trees;
    }

    /**
     * 构建单树
     *
     * @param topId 顶级节点
     * @param sort  是否排序
     * @return T
     */
    public T buildSingleTree(ID topId, boolean sort) {
        List<T> trees = buildTree(topId, sort);
        if (CollUtil.isEmpty(trees)){
            return null;
        }
        return trees.get(0);
    }

    /**
     * 排序树
     *
     * @param tree tree
     */
    public void sortTree(T tree) {
        List<T> children = tree.children();
        if (children != null) {
            children.sort(Comparator.comparing(TreeNode::weight));
            for (T child : children) {
                sortTree(child);
            }
        }
    }

    /**
     * 扁平化树
     *
     * @param tree   tree
     * @param result result
     */
    public void flatTree(T tree, List<T> result) {
        result.add(tree);
        List<T> children = tree.children();
        if (children != null) {
            for (T child : children) {
                flatTree(child, result);
            }
        }
    }

    /**
     * 遍历树
     *
     * @param tree     tree
     * @param consumer consumer
     */
    public void walk(T tree, Consumer<T> consumer) {
        consumer.accept(tree);
        List<T> children = tree.children();
        if (children != null) {
            for (T child : children) {
                walk(child, consumer);
            }
        }
    }
}
