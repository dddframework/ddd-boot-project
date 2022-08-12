package com.github.ddd.common.util.tree;

import com.github.ddd.common.util.JacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树
 *
 * @author ranger
 */
public class TreeUtil {

    /**
     * 构建树
     * @param nodeList
     * @param <T>
     * @return
     */
    public static <T> List<? extends TreeNode<T>> buildTree(List<? extends TreeNode<T>> nodeList, T rootId) {
        Map<T, List<TreeNode<T>>> groupMap = nodeList.stream().collect(Collectors.groupingBy(TreeNode::getPid));
        nodeList.forEach(item -> item.setChildren(groupMap.get(item.getId())));
        return nodeList.stream().filter(menu -> menu.getPid().equals(rootId)).collect(Collectors.toList());
    }

    /**
     * 扁平化树结构
     * @param flatList
     * @param <T>
     * @return
     */
    public static <T> List<? extends TreeNode<T>> flatten(List<? extends TreeNode<T>> flatList){
        return null;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class City implements TreeNode<Integer>{
        private Integer id;
        private String nodeName;
        private Integer pid;
        private List<TreeNode<Integer>> children;
    }


    public static void main(String[] args) {
        List<City> cities = new ArrayList<>();
        cities.add(new City(1, "南昌", 0, null));
        cities.add(new City(2, "青山湖区", 1, null));
        cities.add(new City(3, "红谷滩区", 1, null));
        cities.add(new City(4, "宜春市", 0, null));
        cities.add(new City(5, "宜春市-12", 4, null));
        cities.add(new City(6, "罗家镇", 2, null));

        List<? extends TreeNode<Integer>> list = buildTree(cities, 0);
        System.out.println(JacksonUtil.toPrettyJsonStr(list));

    }

}
