package com.github.ddd.common.exception;

/**
 * A 表示错误来源于用户，比如参数错误，用户安装版本过低，用户支付超时等问题；
 *
 * @author ranger
 */
public class ClientException extends RuntimeException {
    public ClientException(String msg) {
        super(msg);
    }
}
