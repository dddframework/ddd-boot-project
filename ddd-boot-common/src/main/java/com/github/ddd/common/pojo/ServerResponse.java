package com.github.ddd.common.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ddd.common.exception.ErrorCodeEnum;
import lombok.Data;

/**
 * 后端响应
 * 参考 https://beta-pro.ant.design/docs/request-cn#%E7%BB%9F%E4%B8%80%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83
 *
 * @author ranger
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ServerResponse<T> {
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
     * 错误显示方式： 0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
     */
    private String showType;
    /**
     * 方便后端排查：唯一的请求ID
     */
    private String traceId;
    /**
     * 方便后端排查：当前访问服务器的主机
     */
    private String host;


    public interface ShowType {
        /**
         * 静默
         */
        String SILENT = "0";
        /**
         * 警告
         */
        String WARN = "1";
        /**
         * 错误
         */
        String ERROR = "2";
        /**
         * 通知
         */
        String NOTIFICATION = "4";
        /**
         * 另开页面展示
         */
        String PAGE = "9";
    }

    /**
     * 成功响应
     */
    public static ServerResponse<?> createSuccess() {
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(true);
        return response;
    }

    /**
     * 成功响应
     */
    public static <T> ServerResponse<T> createSuccess(T data) {
        ServerResponse<T> response = new ServerResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    /**
     * Silent失败响应
     */
    public static ServerResponse<?> createSilentMsg(String msg) {
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.USER_ERROR.getCode());
        response.setErrorMessage(msg);
        response.setShowType(ShowType.SILENT);
        return response;
    }

    /**
     * Warn失败响应
     */
    public static ServerResponse<?> createWarnMsg(String msg) {
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.USER_ERROR.getCode());
        response.setErrorMessage(msg);
        response.setShowType(ShowType.WARN);
        return response;
    }

    /**
     * Error失败响应
     */
    public static ServerResponse<?> createErrorMsg(String msg) {
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.USER_ERROR.getCode());
        response.setErrorMessage(msg);
        response.setShowType(ShowType.ERROR);
        return response;
    }


    /**
     * Error失败响应
     */
    public static ServerResponse<?> createError(String code, String msg) {
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(code);
        response.setErrorMessage(msg);
        response.setShowType(ShowType.ERROR);
        return response;
    }

    /**
     * notification失败响应
     */
    public static ServerResponse<?> createNotificationMsg(String msg) {
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.USER_ERROR.getCode());
        response.setErrorMessage(msg);
        response.setShowType(ShowType.NOTIFICATION);
        return response;
    }

    /**
     * page失败响应
     */
    public static ServerResponse<?> createPageMsg(String msg) {
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.USER_ERROR.getCode());
        response.setErrorMessage(msg);
        response.setShowType(ShowType.PAGE);
        return response;
    }
}
