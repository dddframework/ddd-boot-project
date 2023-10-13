package com.github.ddd.tinyid.core;

import com.github.ddd.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ranger
 */
@Slf4j
public class TinyIdGenerator {
    protected String bizType;
    protected SegmentIdService segmentIdService;
    protected volatile SegmentId current;
    protected volatile SegmentId next;
    private volatile boolean isLoadingNext;
    private final Object lock = new Object();
    private final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor(new ThreadFactory() {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "TinyIdGenerator-Thread-" + threadNumber.getAndIncrement());
        }
    });


    public TinyIdGenerator(String bizType, SegmentIdService segmentIdService) {
        this.bizType = bizType;
        this.segmentIdService = segmentIdService;
        loadCurrent();
    }

    public synchronized void loadCurrent() {
        if (current == null || !current.useful()) {
            if (next == null) {
                log.info("loadCurrent bizType {}", bizType);
                current = segmentIdService.getNextSegmentId(bizType);
            } else {
                current = next;
                next = null;
            }
        }
    }


    private void loadNext() {
        if (next == null && !isLoadingNext) {
            synchronized (lock) {
                if (next == null && !isLoadingNext) {
                    isLoadingNext = true;
                    THREAD_POOL.submit(() -> {
                        try {
                            log.info("loadNext SegmentId bizType {}", bizType);
                            // 无论获取下个segmentId成功与否，都要将isLoadingNext赋值为false
                            next = segmentIdService.getNextSegmentId(bizType);
                        } finally {
                            isLoadingNext = false;
                        }
                    });
                }
            }
        }
    }

    public Long nextId() {
        while (true) {
            if (current == null) {
                loadCurrent();
                continue;
            }
            Result result = current.nextId();
            if (result.getCode() == Result.OVER) {
                loadCurrent();
            } else {
                if (result.getCode() == Result.LOADING) {
                    loadNext();
                }
                return result.getId();
            }
        }
    }

    public List<Long> nextId(int batchSize) {
        if (batchSize < 1) {
            throw new SystemException("size < 1");
        }
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            Long id = nextId();
            ids.add(id);
        }
        return ids;
    }
}
