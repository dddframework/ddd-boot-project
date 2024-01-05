package com.github.ddd.jdbc.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.exception.SystemException;
import com.github.ddd.common.pojo.AdvancedQuery;
import com.github.ddd.common.pojo.ColumnQuery;
import com.github.ddd.common.pojo.SortingField;
import com.github.ddd.common.pojo.TableData;
import com.github.ddd.common.util.UserContextHolder;
import com.github.ddd.jdbc.util.SqlUtils;
import com.github.ddd.jdbc.util.TableNameParser;
import com.github.ddd.tenant.spring.boot.autoconfigure.TenantProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author ranger
 */
@Slf4j
@RequiredArgsConstructor
public class AdvancedQueryTemplate {

    private final JdbcTemplate jdbcTemplate;
    private final TenantProperties tenantProperties;
    private final AdvancedSqlFactory advancedSqlFactory;

    /**
     * 根据Tag获取真正的SQL
     *
     * @param tag
     * @return
     */
    public String getSqlByTag(String tag) {
        Assert.notNull(tag, "tag param is not null");
        String sql = advancedSqlFactory.getSql(tag);
        if (sql == null) {
            throw new SystemException("目标sql未找到 标记是：" + tag);
        }
        return sql;
    }

    /**
     * 解析真实SQL
     *
     * @param sql
     * @return
     */
    private String parseTrueSql(String sql) {
        Assert.notNull(sql, "sql param is not null");
        if (sql.contains("#")) {
            return getSqlByTag(sql);
        }
        return sql;
    }

    public <T> List<T> selectList(AdvancedQuery query, String sql, Class<T> clazz) {
        Assert.notNull(query, "AdvancedQuery param is not null");
        Assert.notNull(clazz, "Class param is not null");
        String trueSql = parseTrueSql(sql);
        List<Object> args = new ArrayList<>();
        String whereAndSortSql = handleWhereSql(query, args, true);
        String targetSql = changeTable("SELECT * FROM ( " + trueSql + " ) totalData " + whereAndSortSql);
        log.debug("SQL  : {}", targetSql);
        log.debug("Params: {}", args);
        return jdbcTemplate.query(targetSql, new BeanPropertyRowMapper<>(clazz), args.toArray());
    }

    public <T> TableData<T> selectPage(AdvancedQuery query, String sql, Class<T> clazz) {
        Assert.notNull(query, "AdvancedQuery param is not null");
        Assert.notNull(clazz, "Class param is not null");
        int pageNo = query.getPageNo();
        int pageSize = query.getPageSize();
        boolean needPage = pageNo > 0 && pageSize > 0;
        if (!needPage) {
            return TableData.of(selectList(query, sql, clazz));
        }
        String trueSql = parseTrueSql(sql);

        List<Object> args = new ArrayList<>();
        String whereAndSortSql = handleWhereSql(query, args, true);
        String targetSelectSql = changeTable("SELECT * FROM ( " + trueSql + " ) totalData " + whereAndSortSql + " LIMIT ?, ?");
        args.add((pageNo - 1) * pageSize);
        args.add(pageSize);
        log.debug("SQL   : {}", targetSelectSql);
        log.debug("Params: {}", args);
        List<T> list = jdbcTemplate.query(targetSelectSql, new BeanPropertyRowMapper<>(clazz), args.toArray());

        List<Object> args1 = new ArrayList<>();
        String whereSql = handleWhereSql(query, args1, false);
        String targetCountSql = changeTable("SELECT COUNT(*) FROM ( " + trueSql + " ) totalData " + whereSql);
        log.debug("SQL   : {}", targetCountSql);
        log.debug("Params: {}", args1);
        Long totalCount = jdbcTemplate.queryForObject(targetCountSql, Long.class, args1.toArray());

        TableData<T> data = TableData.of(list);
        data.setCurrent(pageNo);
        data.setPageSize(pageSize);
        data.setTotal(totalCount);
        data.setTotalPage(totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1));
        return data;
    }

    private String handleWhereSql(AdvancedQuery query, List<Object> args, boolean sort) {
        List<ColumnQuery> cq = query.getColumnQueries();
        List<SortingField> sortingFields = query.getSortingFields();
        StringBuilder sqlBuilder = new StringBuilder("");
        if (CollUtil.isNotEmpty(cq)) {
            sqlBuilder.append(" WHERE ");
            for (int i = 0; i < cq.size(); i++) {
                ColumnQuery item = cq.get(i);
                if (SqlUtils.check(item.getF())) {
                    throw new SystemException("查询字段不合法");
                }
                item.generateColumnQuerySQL(sqlBuilder, args, i == cq.size() - 1);
            }
        }
        if (CollUtil.isNotEmpty(sortingFields) && sort) {
            sqlBuilder.append(" ORDER BY ");
            for (int i = 0; i < sortingFields.size(); i++) {
                SortingField sortingField = sortingFields.get(i);
                String field = sortingField.getField();
                if (SqlUtils.check(field)) {
                    throw new SystemException("排序字段不合法");
                }
                String order = sortingField.getOrder();
                if (StrUtil.equalsIgnoreCase(order, "ASC") && StrUtil.equalsIgnoreCase(order, "DESC")) {
                    throw new SystemException("排序不合法");
                }
                sqlBuilder.append("`").append(field).append("` ").append(order);
                if (i < sortingFields.size() - 1) {
                    sqlBuilder.append(",");
                }
            }
        }
        if (!isBracketSymmetric(sqlBuilder.toString())) {
            throw new SystemException("括号不匹配");
        }
        return sqlBuilder.toString();
    }


    private boolean isBracketSymmetric(String str) {
        Stack<Character> stack = new Stack<>();
        for (char c : str.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    private String dynamicTableName(String tableName) {
        // 启用SAAS模式 且不是单租户
        if (tenantProperties.isEnable()) {
            Long tenantId = UserContextHolder.getTenantId();
            //`prefix`.`tableName`
            return StrUtil.format("`{}_{}`.`{}`", tenantProperties.getSchemaPrefix(), tenantId, StrUtil.removeAll(tableName, "`"));
        }
        return tableName;
    }

    /**
     * 替换表名
     *
     * @param sql
     * @return
     */
    public String changeTable(String sql) {
        TableNameParser parser = new TableNameParser(sql);
        List<TableNameParser.SqlToken> names = new ArrayList<>();
        parser.accept(names::add);
        StringBuilder builder = new StringBuilder();
        int last = 0;
        for (TableNameParser.SqlToken name : names) {
            int start = name.getStart();
            if (start != last) {
                builder.append(sql, last, start);
                builder.append(dynamicTableName(name.getValue()));
            }
            last = name.getEnd();
        }
        if (last != sql.length()) {
            builder.append(sql.substring(last));
        }
        return builder.toString();
    }
}
