package com.github.ddd.quartz.service;

import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;

/**
 * @author 研发中心-彭幸园
 */
@RequiredArgsConstructor
public class QuartzManager {
    private final Scheduler scheduler;
}
