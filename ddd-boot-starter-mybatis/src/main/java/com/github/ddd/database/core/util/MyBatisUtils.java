package com.github.ddd.database.core.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.ddd.common.pojo.PageParam;
import com.github.ddd.common.pojo.TableData;

/**
 * MyBatis 工具类
 *
 * @author ranger
 */
public class MyBatisUtils {

    /**
     * 分页
     *
     * @param pageParam
     * @param <T>
     * @return
     */
    public static <T> Page<T> buildPage(PageParam pageParam) {
        return new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
    }

    /**
     * 转换IPage
     *
     * @param data
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

}