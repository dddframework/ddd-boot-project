package com.github.ddd.tinyid.core;

import cn.hutool.extra.spring.SpringUtil;

import java.util.List;

/**
 * @author ranger
 */
public class TinyIdUtil {

    private final static String DEFAULT_BIZ_TYPE = "DEFAULT";

    public static Long nextId() {
        return nextId(DEFAULT_BIZ_TYPE);
    }

    public static List<Long> nextId(int batchSize) {
        return nextId(DEFAULT_BIZ_TYPE, batchSize);
    }

    public static Long nextId(String bizType) {
        return SpringUtil.getBean(TinyIdGeneratorFactory.class).getIdGenerator(bizType).nextId();
    }

    public static List<Long> nextId(String bizType, int batchSize) {
        return SpringUtil.getBean(TinyIdGeneratorFactory.class).getIdGenerator(bizType).nextId(batchSize);
    }
}
