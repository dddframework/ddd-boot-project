package com.github.ddd.common.exception;

/**
 * B 表示错误来源于当前系统，往往是业务逻辑出错，或程序健壮性差等问题；
 *
 * @author ranger
 */
public class SystemException extends RuntimeException {
    public SystemException(String msg) {
        super(msg);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
