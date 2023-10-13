package com.github.ddd.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.exception.SystemException;
import com.github.ddd.common.pojo.Tree;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;

/**
 * 树工具
 *
 * @author ranger
 */
@Slf4j
public class TreeUtil {

    private static final int MAX_DEEP = 15;

    /**
     * 递归处理
     *
     * @param tree     tree
     * @param pTreeMap pTreeMap
     * @param deep     deep
     * @param <ID>     ID
     */
    private static <ID> void buildTree(Tree<ID> tree, Map<ID, List<Tree<ID>>> pTreeMap, int deep) {
        ID id = tree.id();
        log.debug("当前物料 {} 递归深度 {}", id, deep);
        if (deep > MAX_DEEP) {
            throw new SystemException("数据不合法，超出最大递归深度" + MAX_DEEP + " 该标识是" + id);
        }
        List<Tree<ID>> childrenTree = pTreeMap.get(id);
        if (childrenTree != null) {
            tree.fillChildren(childrenTree);
            for (Tree<ID> item : childrenTree) {
                buildTree(item, pTreeMap, deep + 1);
            }
        }
    }

    /**
     * 构建树
     *
     * @param dataList 数据集
     * @param topId    顶级节点
     * @param sort     是否排序
     * @param <T>      T
     * @param <ID>     ID
     * @return List<Tree < ID>>
     */
    public static <T extends Tree<ID>, ID> List<Tree<ID>> buildTree(List<T> dataList, ID topId, boolean sort) {
        if (CollUtil.isEmpty(dataList)) {
            return new ArrayList<>();
        }
        // 构建父子关系
        Map<ID, List<Tree<ID>>> pTreeMap = new HashMap<>();
        for (T data : dataList) {
            ID pid = data.pid();
            List<Tree<ID>> trees = pTreeMap.get(pid);
            if (trees == null) {
                List<Tree<ID>> value = new ArrayList<>();
                value.add(data);
                pTreeMap.put(pid, value);
            } else {
                trees.add(data);
                pTreeMap.put(pid, trees);
            }
        }
        List<Tree<ID>> trees = pTreeMap.get(topId);
        if (trees == null) {
            return new ArrayList<>();
        }
        for (Tree<ID> tree : trees) {
            buildTree(tree, pTreeMap, 1);
        }

        if (sort) {
            trees.sort(Comparator.comparing(Tree::weight));
            for (Tree<ID> tree : trees) {
                sortTree(tree);
            }
        }
        return trees;
    }

    /**
     * 排序树
     *
     * @param tree tree
     * @param <ID> ID
     */
    public static <ID> void sortTree(Tree<ID> tree) {
        List<Tree<ID>> children = tree.children();
        if (children != null) {
            children.sort(Comparator.comparing(Tree::weight));
            for (Tree<ID> child : children) {
                sortTree(child);
            }
        }
    }

    /**
     * 打印树
     *
     * @param tree  tree
     * @param level level
     * @param <ID>  ID
     */
    public static <ID> void printTree(Tree<ID> tree, int level) {
        System.out.print(StrUtil.repeat(" ", level));
        System.out.println(tree.name());
        List<Tree<ID>> children = tree.children();
        if (children != null) {
            for (Tree<ID> child : children) {
                printTree(child, level + 1);
            }
        }
    }

    /**
     * 遍历树
     *
     * @param tree     tree
     * @param consumer consumer
     * @param <ID>     ID
     */
    public static <ID> void walk(Tree<ID> tree, Consumer<Tree<ID>> consumer) {
        consumer.accept(tree);
        List<Tree<ID>> children = tree.children();
        if (children != null) {
            for (Tree<ID> child : children) {
                walk(child, consumer);
            }
        }
    }

    /**
     * 扁平化树
     *
     * @param tree   tree
     * @param result result
     * @param <ID>   ID
     */
    public static <ID> void flatTree(Tree<ID> tree, List<Tree<ID>> result) {
        result.add(tree);
        List<Tree<ID>> children = tree.children();
        if (children != null) {
            for (Tree<ID> child : children) {
                flatTree(child, result);
            }
        }
    }
}
