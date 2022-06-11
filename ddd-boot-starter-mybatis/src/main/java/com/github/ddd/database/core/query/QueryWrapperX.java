package com.github.ddd.database.core.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.Collection;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @param <T> 数据类型
 * @author ranger
 */
public class QueryWrapperX<T> extends QueryWrapper<T> {

    /**
     * LIKE
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> likeIfPresent(String column, String val) {
        if (StringUtils.isNotBlank(val)) {
            return (QueryWrapperX<T>) super.like(column, val);
        }
        return this;
    }

    /**
     * NOT LIKE
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> notLikeIfPresent(String column, String val) {
        if (StringUtils.isNotBlank(val)) {
            return (QueryWrapperX<T>) super.notLike(column, val);
        }
        return this;
    }

    /**
     * 左LIKE
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> likeLeftIfPresent(String column, String val) {
        if (StringUtils.isNotBlank(val)) {
            return (QueryWrapperX<T>) super.likeLeft(column, val);
        }
        return this;
    }

    /**
     * 右LIKE
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> likeRightIfPresent(String column, String val) {
        if (StringUtils.isNotBlank(val)) {
            return (QueryWrapperX<T>) super.likeRight(column, val);
        }
        return this;
    }

    /**
     * IN
     *
     * @param column
     * @param values
     * @return
     */
    public QueryWrapperX<T> inIfPresent(String column, Collection<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            return (QueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    /**
     * NOT IN
     *
     * @param column
     * @param values
     * @return
     */
    public QueryWrapperX<T> notInIfPresent(String column, Collection<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            return (QueryWrapperX<T>) super.notIn(column, values);
        }
        return this;
    }

    /**
     * IN
     *
     * @param column
     * @param values
     * @return
     */
    public QueryWrapperX<T> inIfPresent(String column, Object... values) {
        if (!ArrayUtils.isEmpty(values)) {
            return (QueryWrapperX<T>) super.in(column, values);
        }
        return this;
    }

    /**
     * NOT IN
     *
     * @param column
     * @param values
     * @return
     */
    public QueryWrapperX<T> notInIfPresent(String column, Object... values) {
        if (!ArrayUtils.isEmpty(values)) {
            return (QueryWrapperX<T>) super.notIn(column, values);
        }
        return this;
    }

    /**
     * 相等 忽略空白字符串
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> eqIfPresentAndNotBlankStr(String column, Object val) {
        if (val != null) {
            if (val instanceof String) {
                String b = (String) val;
                if (StringUtils.isNotBlank(b)) {
                    return (QueryWrapperX<T>) super.eq(column, val);
                }
            } else {
                return (QueryWrapperX<T>) super.eq(column, val);
            }
        }
        return this;
    }

    /**
     * 相等 不忽略空白字符串
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> eqIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.eq(column, val);
        }
        return this;
    }

    /**
     * 不等
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> neIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.ne(column, val);
        }
        return this;
    }

    /**
     * 大于
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> gtIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.gt(column, val);
        }
        return this;
    }

    /**
     * 大于等于
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> geIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.ge(column, val);
        }
        return this;
    }

    /**
     * 小于
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> ltIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.lt(column, val);
        }
        return this;
    }

    /**
     * 小于等于
     *
     * @param column
     * @param val
     * @return
     */
    public QueryWrapperX<T> leIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapperX<T>) super.le(column, val);
        }
        return this;
    }

    /**
     * BETWEEN
     *
     * @param column
     * @param val1
     * @param val2
     * @return
     */
    public QueryWrapperX<T> betweenIfPresent(String column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (QueryWrapperX<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (QueryWrapperX<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (QueryWrapperX<T>) le(column, val2);
        }
        return this;
    }

    /**
     * NOT BETWEEN
     *
     * @param column
     * @param val1
     * @param val2
     * @return
     */
    public QueryWrapperX<T> notBetweenIfPresent(String column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (QueryWrapperX<T>) super.notBetween(column, val1, val2);
        }
        if (val1 != null) {
            return (QueryWrapperX<T>) le(column, val1);
        }
        if (val2 != null) {
            return (QueryWrapperX<T>) ge(column, val2);
        }
        return this;
    }


    // ========== 重写父类方法，方便链式调用 ==========

    @Override
    public QueryWrapperX<T> eq(boolean condition, String column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> eq(String column, Object val) {
        super.eq(column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> ne(String column, Object val) {
        super.ne(column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> orderByDesc(String column) {
        super.orderByDesc(true, column);
        return this;
    }

    @Override
    public QueryWrapperX<T> orderByAsc(String column) {
        super.orderByAsc(true, column);
        return this;
    }

    @Override
    public QueryWrapperX<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

    @Override
    public QueryWrapperX<T> in(String column, Collection<?> coll) {
        super.in(column, coll);
        return this;
    }

    @Override
    public QueryWrapperX<T> notIn(String column, Collection<?> coll) {
        super.notIn(column, coll);
        return this;
    }


    @Override
    public QueryWrapperX<T> like(String column, Object val) {
        super.like(column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> notLike(String column, Object val) {
        super.notLike(column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> likeLeft(String column, Object val) {
        super.likeLeft(column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> likeRight(String column, Object val) {
        super.likeRight(column, val);
        return this;
    }

    @Override
    public QueryWrapperX<T> between(String column, Object val1, Object val2) {
        super.between(column, val1, val2);
        return this;
    }

    @Override
    public QueryWrapperX<T> notBetween(String column, Object val1, Object val2) {
        super.notBetween(column, val1, val2);
        return this;
    }
}