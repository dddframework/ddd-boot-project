package com.github.ddd.mybatis.core.util;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.ddd.common.pojo.PageParam;
import com.github.ddd.common.pojo.SortingField;
import com.github.ddd.common.pojo.TableData;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * MyBatis 工具类
 *
 * @author ranger
 */
public class MyBatisUtils {

    /**
     * 不分页
     *
     * @param <T>
     * @return
     */
    public static <T> Page<T> buildNoPage() {
        Page<T> page = new Page<>();
        page.setSize(-1L);
        page.setOptimizeCountSql(false);
        page.setOptimizeJoinOfCountSql(false);
        return page;
    }


    /**
     * 分页
     *
     * @param pageParam
     * @param <T>
     * @return
     */
    public static <T> Page<T> buildPage(PageParam pageParam) {
        if (pageParam.getPageNo() <= 0) {
            return buildNoPage();
        }
        return new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
    }

    /**
     * 转换IPage
     *
     * @param <T>
     * @return
     */
    public static <T> TableData<T> toTableData(IPage<T> data, PageParam pageParam) {
        TableData<T> tableData = new TableData<>();
        tableData.setCurrent(pageParam.getPageNo());
        tableData.setPageSize(pageParam.getPageSize());
        tableData.setTotal(data.getTotal());
        tableData.setList(data.getRecords());
        return tableData;
    }

    /**
     * 分页+排序
     *
     * @param pageParam
     * @param sortingFields
     * @param <T>
     * @return
     */
    public static <T> Page<T> buildPage(PageParam pageParam, Collection<SortingField> sortingFields) {
        // 页码 + 数量
        Page<T> page = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        page.setOptimizeCountSql(false);
        page.setOptimizeJoinOfCountSql(false);
        // 排序字段
        if (!CollectionUtil.isEmpty(sortingFields)) {
            page.addOrder(sortingFields.stream().map(sortingField -> SortingField.ORDER_ASC.equals(sortingField.getOrder()) ?
                            OrderItem.asc(sortingField.getField()) : OrderItem.desc(sortingField.getField()))
                    .collect(Collectors.toList()));
        }
        return page;
    }

}
