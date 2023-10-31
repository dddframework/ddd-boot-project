package com.github.ddd.tinyid.core;

import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.exception.SystemException;
import com.github.ddd.common.util.UserContextHolder;
import com.github.ddd.tinyid.spring.boot.autoconfigure.TenantProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ranger
 */
@Slf4j
@RequiredArgsConstructor
public class SegmentIdService {

    /**
     * 预加载下个号段的百分比
     */
    public static final int LOADING_PERCENT = 20;

    private final JdbcTemplate jdbcTemplate;
    private final TenantProperties tenantProperties;

    private final static String QUERY_TINY_ID = "select * from %s where biz_type = ? FOR UPDATE";
    private final static String UPDATE_TINY_ID = "update %s set max_id = ?, update_time= ? where max_id = ? and biz_type = ?";

    private String geTinyIdTable() {
        String tinyIdTable = tenantProperties.getTinyIdTable();
        // 启用多租户模式
        if (tenantProperties.isEnable()) {
            String prefix = tenantProperties.getSchemaPrefix();
            if (StrUtil.isBlank(prefix)) {
                throw new RuntimeException("多租户模式 前缀不能为空");
            }
            Long tenantId = UserContextHolder.getCurrentUser().getTenantId();
            //`prefix`.`tableName`
            return StrUtil.format("`{}{}`.`{}`", prefix, tenantId, tinyIdTable);
        }
        return tinyIdTable;
    }

    /**
     * 根据bizType获取下一个SegmentId对象
     *
     * @param bizType bizType
     * @return SegmentId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SegmentId getNextSegmentId(String bizType) {
        TinyId tinyId = this.queryByBizType(bizType);
        if (tinyId == null) {
            throw new SystemException("can not find bizType:" + bizType);
        }
        Long newMaxId = tinyId.getMaxId() + tinyId.getStep();
        Long oldMaxId = tinyId.getMaxId();
        this.updateMaxId(newMaxId, oldMaxId, tinyId.getBizType());
        tinyId.setMaxId(newMaxId);
        return convert(tinyId);
    }

    /**
     * 查询 TinyId
     *
     * @param bizType bizType
     * @return TinyId
     */
    public TinyId queryByBizType(String bizType) {
        return jdbcTemplate.queryForObject(String.format(QUERY_TINY_ID, geTinyIdTable()), new BeanPropertyRowMapper<>(TinyId.class), bizType);
    }

    /**
     * 更新 TinyId
     *
     * @param newMaxId newMaxId
     * @param oldMaxId oldMaxId
     * @param bizType  bizType
     */
    public void updateMaxId(Long newMaxId, Long oldMaxId, String bizType) {
        int update = jdbcTemplate.update(String.format(UPDATE_TINY_ID, geTinyIdTable()), newMaxId, System.currentTimeMillis(), oldMaxId, bizType);
        if (update > 0) {
            log.debug("updateMaxId success bizType {} newMaxId {}", bizType, newMaxId);
        }
    }

    /**
     * 转换 SegmentId
     *
     * @param tinyId TinyId
     * @return SegmentId
     */
    public SegmentId convert(TinyId tinyId) {
        SegmentId segmentId = new SegmentId();
        segmentId.setCurrentId(new AtomicLong(tinyId.getMaxId() - tinyId.getStep()));
        segmentId.setMaxId(tinyId.getMaxId());
        segmentId.setRemainder(tinyId.getRemainder());
        segmentId.setDelta(tinyId.getDelta());
        // 默认20%加载
        segmentId.setLoadingId(segmentId.getCurrentId().get() + tinyId.getStep() * LOADING_PERCENT / 100);
        return segmentId;
    }


}
