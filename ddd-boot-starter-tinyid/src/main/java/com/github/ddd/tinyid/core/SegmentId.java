package com.github.ddd.tinyid.core;

import com.github.ddd.common.exception.SystemException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author du_imba
 * nextId: (currentId, maxId]
 */
@Slf4j
@Data
public class SegmentId {
    private long maxId;
    private long loadingId;
    private AtomicLong currentId;
    /**
     * increment by
     */
    private int delta;
    /**
     * mod num
     */
    private int remainder;

    private volatile boolean init;

    /**
     * 这个方法主要为了1,4,7,10...这种序列准备的
     * 设置好初始值之后，会以delta的方式递增，保证无论开始id是多少都能生成正确的序列
     * 如当前是号段是(1000,2000]，delta=3, remainder=0，则经过这个方法后，currentId会先递增到1002,之后每次增加delta
     * 因为currentId会先递增，所以会浪费一个id，所以做了一次减delta的操作，实际currentId会从999开始增，第一个id还是1002
     */
    public synchronized void init() {
        if (init) {
            return;
        }
        long id = currentId.get();
        if (id % delta == remainder) {
            init = true;
            return;
        }
        for (int i = 0; i <= delta; i++) {
            id = currentId.incrementAndGet();
            if (id % delta == remainder) {
                // 避免浪费 减掉系统自己占用的一个id
                currentId.addAndGet(-delta);
                init = true;
                return;
            }
        }
        if (!init) {
            log.debug("SegmentId init error id {} delta {} remainder {}", id, delta, remainder);
            throw new SystemException("SegmentId init error");
        }
    }

    public Result nextId() {
        init();
        long id = currentId.addAndGet(delta);
        if (id > maxId) {
            log.debug("超过maxId 不可用 SegmentId id {} maxId {}", id, maxId);
            return new Result(Result.OVER, id);
        }
        if (id >= loadingId) {
            return new Result(Result.LOADING, id);
        }
        return new Result(Result.NORMAL, id);
    }

    /**
     * 使用完了
     *
     * @return boolean
     */
    public boolean useful() {
        return currentId.get() <= maxId;
    }

}
