package com.github.ddd.mybatis.core.tenant;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.TableNameParser;
import com.github.ddd.common.util.UserContextHolder;
import com.github.ddd.mybatis.spring.boot.autoconfigure.MybatisProperties;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ranger
 */
@RequiredArgsConstructor
public class TenantDbHandler {

    private final MybatisProperties mybatisProperties;

    /**
     * 获取真实租户数据表
     *
     * @param tableName
     * @return
     */
    public String parseTrueTableName(String tableName) {
        // 已经指定表名 且是单租户模式
        if (StrUtil.contains(tableName, ".") || !mybatisProperties.isEnableSaas()) {
            return tableName;
        }
        // 启用SAAS模式
        Long tenantId = UserContextHolder.getTenantId();
        //`prefix`.`tableName`
        return StrUtil.format("`{}{}`.`{}`", mybatisProperties.getSchemaPrefix(), tenantId, StrUtil.removeAll(tableName, "`"));
    }

    /**
     * 替换SQL中关于租户的数据表名
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
                builder.append(parseTrueTableName(name.getValue()));
            }
            last = name.getEnd();
        }
        if (last != sql.length()) {
            builder.append(sql.substring(last));
        }
        return builder.toString();
    }
}
