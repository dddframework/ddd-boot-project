package com.github.ddd.mybatis.core.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.ddd.common.pojo.PageParam;
import com.github.ddd.common.pojo.TableData;
import com.github.ddd.mybatis.core.util.MyBatisUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 在 MyBatis Plus 的 BaseMapper 的基础上拓展，提供更多的能力
 *
 * @author ranger
 */
public interface BaseMapperX<T> extends BaseMapper<T> {

    /**
     * 分页查询
     *
     * @param pageParam
     * @param queryWrapper
     * @return
     */
    default TableData<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
        IPage<T> mpPage = MyBatisUtils.buildPage(pageParam);
        selectPage(mpPage, queryWrapper);
        TableData<T> tableData = new TableData<>();
        tableData.setCurrent(pageParam.getPageNo());
        tableData.setPageSize(pageParam.getPageSize());
        tableData.setTotal(mpPage.getTotal());
        tableData.setList(mpPage.getRecords());
        return tableData;
    }

    /**
     * 查询单个
     *
     * @param field
     * @param value
     * @return
     */
    default T selectOne(String field, Object value) {
        return selectOne(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * 单个条件统计查询
     *
     * @param field
     * @param value
     * @return
     */
    default long selectCount(String field, Object value) {
        return selectCount(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * 单个条件删除
     *
     * @param field
     * @param value
     * @return
     */
    default int delete(String field, Object value) {
        return delete(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * 单个条件IN删除
     *
     * @param field
     * @param value
     * @return
     */
    default int deleteIn(String field, Collection<?> value) {
        return delete(new QueryWrapper<T>().in(field, value));
    }


    /**
     * 查询全部
     *
     * @return
     */
    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    /**
     * 单条件查询
     *
     * @param field
     * @param value
     * @return
     */
    default List<T> selectList(String field, Object value) {
        return selectList(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * 单条件IN查询
     *
     * @param field
     * @param value
     * @return
     */
    default List<T> selectListIn(String field, Collection<?> value) {
        return selectList(new QueryWrapper<T>().in(field, value));
    }
}