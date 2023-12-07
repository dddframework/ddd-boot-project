package com.github.ddd.common.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ddd.common.exception.ErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端响应
 * 参考 https://beta-pro.ant.design/docs/request-cn#%E7%BB%9F%E4%B8%80%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83
 *
 * @author ranger
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ServerResponse<T> implements Serializable {
    /**
     * 请求是否成功
     */
    private boolean success;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 展示给用户的错误信息
     */
    private String errorMessage;
    /**
     * 方便后端排查：唯一的请求ID
     */
    private String traceId;
    /**
     * 方便后端排查：当前访问服务器的主机
     */
    private String host;

    /**
     * 成功响应
     */
    public static <T> ServerResponse<T> createSuccess(T data) {
        ServerResponse<T> response = new ServerResponse<>();
        response.setSuccess(true);
        response.setErrorCode(ErrorCodeEnum.SUCCESS.getCode());
        response.setErrorMessage(ErrorCodeEnum.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    /**
     * 成功响应
     */
    public static ServerResponse<?> createSuccess() {
        return createSuccess(null);
    }

    /**
     * Error失败响应
     */
    public static ServerResponse<?> createErrorMsg(String msg) {
        return createError(ErrorCodeEnum.SYSTEM_ERROR.getCode(), msg);
    }


    /**
     * 创建失败响应消息
     *
     * @param msg
     * @return
     */
    public static ServerResponse<?> createError(String code, String msg) {
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(code);
        response.setErrorMessage(msg);
        return response;
    }
}
