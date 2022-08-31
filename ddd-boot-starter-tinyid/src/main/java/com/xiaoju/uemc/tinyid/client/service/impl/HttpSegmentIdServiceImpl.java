package com.xiaoju.uemc.tinyid.client.service.impl;

import com.xiaoju.uemc.tinyid.base.entity.SegmentId;
import com.xiaoju.uemc.tinyid.base.service.SegmentIdService;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientProperties;
import com.xiaoju.uemc.tinyid.client.utils.TinyIdHttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author du_imba
 */
@Slf4j
@RequiredArgsConstructor
public class HttpSegmentIdServiceImpl implements SegmentIdService {

    private final TinyIdClientProperties tinyIdClientProperties;

    @Override
    public SegmentId getNextSegmentId(String bizType) {
        String serverUrl = "http://{0}/tinyid/id/nextSegmentIdSimple?token={1}&bizType=";
        String url = MessageFormat.format(serverUrl, tinyIdClientProperties.getTinyIdServer(), tinyIdClientProperties.getTinyIdToken());
        String response = TinyIdHttpUtils.post(url, tinyIdClientProperties.getReadTimeout(), tinyIdClientProperties.getConnectTimeout());
        log.info("tinyId client getNextSegmentId end, response:" + response);
        if (response == null || "".equals(response.trim())) {
            return null;
        }
        SegmentId segmentId = new SegmentId();
        String[] arr = response.split(",");
        segmentId.setCurrentId(new AtomicLong(Long.parseLong(arr[0])));
        segmentId.setLoadingId(Long.parseLong(arr[1]));
        segmentId.setMaxId(Long.parseLong(arr[2]));
        segmentId.setDelta(Integer.parseInt(arr[3]));
        segmentId.setRemainder(Integer.parseInt(arr[4]));
        return segmentId;
    }


}
