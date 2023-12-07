package com.github.ddd.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举,参考阿里巴巴代码规范
 * 错误码为字符串类型，共 5 位，分成两个部分：错误产生来源+四位数字编号
 * 错误产生来源分为 A/B/C，
 * A 表示错误来源于用户，比如参数错误，用户安装版本过低，用户支付超时等问题；
 * B 表示错误来源于当前系统，往往是业务逻辑出错，或程序健壮性差等问题；
 * C 表示错误来源于第三方服务，比如 CDN 服务出错，消息投递超时等问题；
 * 四位数字编号从 0001 到 9999，大类之间的步长间距预留 100
 *
 * @author ranger
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    /**
     * 正常
     */
    SUCCESS("00000", "成功"),
    /**
     * 系统执行出错
     */
    SYSTEM_ERROR("B0001", "系统执行出错"),
    ;

    /**
     * 错误码
     */
    private final String code;
    /**
     * 简单描述
     */
    private final String description;
}
