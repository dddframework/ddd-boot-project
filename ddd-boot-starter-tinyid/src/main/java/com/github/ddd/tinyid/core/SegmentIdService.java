package com.github.ddd.tinyid.core;

import com.github.ddd.common.exception.SystemException;
import com.github.ddd.tenant.core.TenantDbHandler;
import com.github.ddd.tinyid.spring.boot.autoconfigure.TinyIdProperties;
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
    private final TinyIdProperties tinyIdProperties;
    private final TenantDbHandler tenantDbHandler;

    private final static String QUERY_TINY_ID = "select * from %s where biz_type = ? FOR UPDATE";
    private final static String UPDATE_TINY_ID = "update %s set max_id = ?, update_time= ? where max_id = ? and biz_type = ?";


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
    private TinyId queryByBizType(String bizType) {
        return jdbcTemplate.queryForObject(String.format(QUERY_TINY_ID, tenantDbHandler.parseTrueTableName(tinyIdProperties.getTinyIdTable())), new BeanPropertyRowMapper<>(TinyId.class), bizType);
    }

    /**
     * 更新 TinyId
     *
     * @param newMaxId newMaxId
     * @param oldMaxId oldMaxId
     * @param bizType  bizType
     */
    private void updateMaxId(Long newMaxId, Long oldMaxId, String bizType) {
        int update = jdbcTemplate.update(String.format(UPDATE_TINY_ID, tenantDbHandler.parseTrueTableName(tinyIdProperties.getTinyIdTable())), newMaxId, System.currentTimeMillis(), oldMaxId, bizType);
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
    private SegmentId convert(TinyId tinyId) {
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
