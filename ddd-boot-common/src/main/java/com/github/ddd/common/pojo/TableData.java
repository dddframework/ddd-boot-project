package com.github.ddd.common.pojo;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * 表格数据封装
 * 参考 https://beta-pro.ant.design/docs/request-cn#%E7%BB%9F%E4%B8%80%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83
 *
 * @author ranger
 */
@Data
public class TableData<T> {
    /**
     * 当前页
     */
    private int current;
    /**
     * 分页大小
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 数据
     */
    private Collection<T> list;

    /**
     * 空表格
     *
     * @param <T> T
     * @return T
     */
    public static <T> TableData<T> empty() {
        TableData<T> tableData = new TableData<>();
        tableData.setCurrent(1);
        tableData.setTotal(0);
        tableData.setPageSize(0);
        tableData.setList(new ArrayList<>());
        return tableData;
    }

    /**
     * 转换集合数据
     *
     * @param list Collection
     * @param <T>  T
     * @return T
     */
    public static <T> TableData<T> of(Collection<T> list) {
        if (CollUtil.isEmpty(list)) {
            return empty();
        }
        TableData<T> tableData = new TableData<>();
        tableData.setCurrent(1);
        tableData.setTotal(list.size());
        tableData.setPageSize(list.size());
        tableData.setList(list);
        return tableData;
    }

    /**
     * 转换 TableData
     *
     * @param mapper Function
     * @param <E>    E
     * @return E
     */
    public <E> TableData<E> transform(Function<T, E> mapper) {
        TableData<E> newTableData = new TableData<>();
        newTableData.setPageSize(this.getPageSize());
        newTableData.setCurrent(this.getCurrent());
        newTableData.setTotal(this.getTotal());
        List<E> list = new ArrayList<>();
        newTableData.setList(list);
        if (CollUtil.isNotEmpty(this.list)) {
            for (T t : this.list) {
                E newE = mapper.apply(t);
                list.add(newE);
            }
        }
        return newTableData;
    }
}
