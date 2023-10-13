package com.github.ddd.tinyid.core;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ranger
 */
@RequiredArgsConstructor
public class TinyIdGeneratorFactory {

    private final SegmentIdService segmentIdService;

    private static final ConcurrentHashMap<String, TinyIdGenerator> GENERATORS = new ConcurrentHashMap<>();

    public TinyIdGenerator getIdGenerator(String bizType) {
        if (GENERATORS.containsKey(bizType)) {
            return GENERATORS.get(bizType);
        }
        synchronized (this) {
            if (GENERATORS.containsKey(bizType)) {
                return GENERATORS.get(bizType);
            }
            TinyIdGenerator idGenerator = new TinyIdGenerator(bizType, segmentIdService);
            GENERATORS.put(bizType, idGenerator);
            return idGenerator;
        }
    }
}
