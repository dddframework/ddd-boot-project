package com.github.ddd.common.pojo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ranger
 */
@Data
public class AdvancedQuery {
    /**
     * 页码 <1 表示不分页
     */
    private int pageNo;
    /**
     * 分页大小
     */
    private int pageSize;
    /**
     * 列查询
     */
    private List<ColumnQuery> columnQueries = new ArrayList<>();
    /**
     * 排序字段
     */
    private List<SortingField> sortingFields = new ArrayList<>();

    /**
     * 追加查询条件
     *
     * @param f
     * @param o
     * @param v
     * @return
     */
    public AdvancedQuery appendWhere(String f, String o, Object v) {
        if (v == null) {
            return this;
        } else if (v instanceof Collection && ((Collection<?>) v).size() <= 0) {
            return this;
        } else if (v instanceof CharSequence && StrUtil.isBlank((CharSequence) v)) {
            return this;
        } else {
            ColumnQuery e = new ColumnQuery();
            e.setF(f);
            e.setO(o);
            e.setV(v);
            e.setT("AND");
            columnQueries.add(0, e);
            return this;
        }
    }

    /**
     * 多字段OR查询
     *
     * @param ff
     * @param o
     * @param v
     * @return
     */
    public AdvancedQuery appendWhereOr(List<String> ff, String o, Object v) {
        if (v == null) {
            return this;
        } else if (v instanceof Collection && ((Collection<?>) v).size() <= 0) {
            return this;
        } else if (v instanceof CharSequence && StrUtil.isBlank((CharSequence) v)) {
            return this;
        } else {
            List<ColumnQuery> cc = new ArrayList<>();
            for (int i = 0; i < ff.size(); i++) {
                String f = ff.get(i);
                if (i == 0) {
                    ColumnQuery c1 = new ColumnQuery("(", f, o, v, "", "OR");
                    cc.add(c1);
                } else if (i == ff.size() - 1) {
                    ColumnQuery c1 = new ColumnQuery("", f, o, v, ")", "AND");
                    cc.add(c1);
                } else {
                    ColumnQuery c1 = new ColumnQuery("", f, o, v, "", "OR");
                    cc.add(c1);
                }
            }
            for (int i = cc.size() - 1; i >= 0; i--) {
                columnQueries.add(0, cc.get(i));
            }
            return this;
        }
    }

    /**
     * 追加排序条件
     *
     * @param f
     * @return
     */
    public AdvancedQuery appendOrderAsc(String f) {
        SortingField e = new SortingField();
        e.setField(f);
        e.setOrder("ASC");
        sortingFields.add(e);
        return this;
    }

    /**
     * 追加排序条件
     *
     * @param f
     * @return
     */
    public AdvancedQuery appendOrderDesc(String f) {
        SortingField e = new SortingField();
        e.setField(f);
        e.setOrder("DESC");
        sortingFields.add(e);
        return this;
    }
}
