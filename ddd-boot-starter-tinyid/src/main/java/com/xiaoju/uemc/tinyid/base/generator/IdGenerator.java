package com.xiaoju.uemc.tinyid.base.generator;

import java.util.List;

/**
 * @author du_imba
 */
public interface IdGenerator {
    /**
     * get next id
     *
     * @return Long
     */
    Long nextId();

    /**
     * get next id batch
     *
     * @param batchSize batchSize
     * @return List<Long>
     */
    List<Long> nextId(Integer batchSize);
}
