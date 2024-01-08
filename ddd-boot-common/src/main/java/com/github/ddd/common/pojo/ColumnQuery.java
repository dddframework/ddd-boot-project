package com.github.ddd.common.pojo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.exception.SystemException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ranger
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ColumnQuery {
    /**
     * 左括号
     */
    private String lp = "";
    /**
     * 字段
     */
    private String f;
    /**
     * 条件符号
     */
    private String o;
    /**
     * 值
     */
    private Object v;
    /**
     * 左括号
     */
    private String rp = "";
    /**
     * 查询类型，可以是AND或者OR
     */
    private String t;

    /**
     * 生成查询SQL语句
     *
     * @param sqlBuilder
     * @param args
     * @param isLast
     */
    public void generateColumnQuerySQL(StringBuilder sqlBuilder, List<Object> args, boolean isLast) {
        if (!LP.contains(lp) || !RP.contains(rp) || !OP.contains(o) || !TYPE.contains(t)) {
            throw new SystemException("符号不合法");
        }
        if (StrUtil.equalsIgnoreCase(o, "IS NULL") ||
                StrUtil.equalsIgnoreCase(o, "IS NOT NULL")
        ) {
            sqlBuilder.append(lp).append(" `").append(f).append("` ").append(o).append(" ").append(rp);
        } else if (v == null) {
            throw new SystemException("比较值不能为空");
        }
        if (o.contains("IN") && !(v instanceof Collection)) {
            throw new SystemException("IN查询必须传入集合参数");
        }
        if (v instanceof Collection) {
            Collection<?> collection = (Collection<?>) this.v;
            if (collection.size() > 0) {
                sqlBuilder.append(lp).append(" `").append(f).append("` ").append(o).append(" ").append(buildInSqlStr(collection)).append(" ").append(rp);
                args.addAll(collection);
            } else {
                throw new SystemException("IN 比较值不能为空");
            }
        } else if (!o.contains("NULL")) {
            String op = o;
            if (op.contains("LIKE")) {
                op = StrUtil.replace(op, "LEFT", "");
                op = StrUtil.replace(op, "RIGHT", "");
            }
            sqlBuilder.append(lp).append(" `").append(f).append("` ").append(op).append(" ? ").append(rp);
            if (StrUtil.contains(o, "LIKE")) {
                args.add(handleLikeQueryField(v.toString(), o));
            } else {
                args.add(v);
            }
        }
        if (!isLast) {
            sqlBuilder.append(" ").append(t).append(" ");
        }
    }

    /**
     * 处理模糊查询 特殊字符
     *
     * @param key  String
     * @param like String
     * @return String
     */
    public String handleLikeQueryField(String key, String like) {
        if (StrUtil.isNotBlank(key)) {
            // 默认的转义字符是反斜杠"\" 可以使用ESCAPE子句指定转义字符
            key = key.replaceAll("\\\\", "\\\\\\\\");
            key = key.replaceAll("_", "\\\\_");
            key = key.replaceAll("%", "\\\\%");
            if (like.contains("LEFT")) {
                return key + "%";
            } else if (like.contains("RIGHT")) {
                return "%" + key;
            } else {
                return "%" + key + "%";
            }
        }
        return "";
    }


    /**
     * 构建IN占位SQL
     *
     * @param list List
     * @return String
     */
    public String buildInSqlStr(Collection<?> list) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            temp.add("?");
        }
        return temp.toString().replace("[", "(").replace("]", ")").replace(", ", ",");
    }

    public static final List<String> LP = ListUtil.of("", "(", "((", "(((");
    public static final List<String> RP = ListUtil.of("", ")", "))", ")))");
    public static final List<String> TYPE = ListUtil.of("AND", "OR");
    public static final List<String> OP = ListUtil.of(">", ">=", "<", "<=", "=", "!=",
            "LIKE", "LEFT LIKE", "RIGHT LIKE", "NOT LIKE", "NOT LEFT LIKE", "NOT RIGHT LIKE",
            "IN", "NOT IN", "IS NULL", "IS NOT NULL");

}
