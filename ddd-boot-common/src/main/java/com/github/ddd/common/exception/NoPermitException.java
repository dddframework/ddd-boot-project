package com.github.ddd.common.exception;

/**
 * 没权限异常
 *
 * @author ranger
 */
public class NoPermitException extends ClientException {
    public NoPermitException(String msg) {
        super(msg);
    }
}
