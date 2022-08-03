package com.github.ddd.common.exception;

/**
 * C 表示错误来源于第三方服务，比如 CDN 服务出错，消息投递超时等问题；
 * @author ranger
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
