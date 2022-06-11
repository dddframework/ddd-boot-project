package com.github.ddd.common.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * 转换集合数据
     *
     * @param list Collection
     * @param <T> T
     * @return T
     */
    public static <T> TableData<T> of(Collection<T> list) {
        TableData<T> tableData = new TableData<>();
        tableData.setCurrent(1);
        tableData.setTotal(list == null ? 0 : list.size());
        tableData.setPageSize(list == null ? 0 : list.size());
        tableData.setList(list == null ? new ArrayList<>() : list);
        return tableData;
    }

    /**
     * 转换 TableData
     *
     * @param mapper Function
     * @param <E> E
     * @return E
     */
    public <E> TableData<E> transform(Function<T, E> mapper) {
        TableData<E> newTableData = new TableData<>();
        newTableData.setPageSize(this.getPageSize());
        newTableData.setCurrent(this.getCurrent());
        newTableData.setTotal(this.getTotal());
        if (this.getList() != null) {
            newTableData.setList(this.getList().stream().map(mapper).collect(Collectors.toList()));
        }
        return newTableData;
    }
}
