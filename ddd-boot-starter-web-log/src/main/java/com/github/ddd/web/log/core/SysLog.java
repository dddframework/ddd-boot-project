package com.github.ddd.web.log.core;

import lombok.Data;

import java.util.Date;

/**
 * @author ranger
 */
@Data
public class SysLog {
    /**
     * 0成功 1失败 2出现非预期异常
     */
    public static final String OK = "0";
    public static final String FAIL = "1";
    /**
     * ID
     */
    private Long id;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 操作用户
     */
    private Long userId;
    /**
     * 操作用户
     */
    private String nickname;
    /**
     * 请求路径
     */
    private String requestUri;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 客户端信息
     */
    private String userAgent;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 本次操作结果 0成功 1失败
     */
    private String result;
    /**
     * 耗时
     */
    private Long time;
    /**
     * 异常情况
     */
    private String exception;
    /**
     * 创建时间
     */
    private Date createTime;
}
