package com.github.ddd.common.exception;

/**
 * 未登录异常
 *
 * @author ranger
 */
public class NoLoginException extends ClientException {
    public NoLoginException(String msg) {
        super(msg);
    }
}
