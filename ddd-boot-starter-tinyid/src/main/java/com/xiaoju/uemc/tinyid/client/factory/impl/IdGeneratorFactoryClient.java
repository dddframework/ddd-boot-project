package com.xiaoju.uemc.tinyid.client.factory.impl;

import com.xiaoju.uemc.tinyid.base.factory.AbstractIdGeneratorFactory;
import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.base.generator.impl.CachedIdGenerator;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientProperties;
import com.xiaoju.uemc.tinyid.client.service.impl.HttpSegmentIdServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author du_imba
 */
@Slf4j
@RequiredArgsConstructor
public class IdGeneratorFactoryClient extends AbstractIdGeneratorFactory {

    private final TinyIdClientProperties tinyIdClientProperties;

    @Override
    protected IdGenerator createIdGenerator(String bizType) {
        return new CachedIdGenerator(bizType, new HttpSegmentIdServiceImpl(tinyIdClientProperties));
    }

}
