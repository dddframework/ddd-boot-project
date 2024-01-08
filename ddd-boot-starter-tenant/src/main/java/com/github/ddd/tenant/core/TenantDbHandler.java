package com.github.ddd.tenant.core;

import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.util.UserContextHolder;
import com.github.ddd.tenant.spring.boot.autoconfigure.TenantProperties;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ranger
 */
@RequiredArgsConstructor
public class TenantDbHandler {

    private final TenantProperties tenantProperties;

    /**
     * 获取真实租户数据表
     *
     * @param tableName
     * @return
     */
    public String parseTrueTableName(String tableName) {
        // 启用SAAS模式 且不是单租户
        if (tenantProperties.isEnable()) {
            Long tenantId = UserContextHolder.getTenantId();
            //`prefix`.`tableName`
            return StrUtil.format("`{}{}`.`{}`", tenantProperties.getSchemaPrefix(), tenantId, StrUtil.removeAll(tableName, "`"));
        }
        return tableName;
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
