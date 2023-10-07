package com.github.ddd.web.exception;

import com.github.ddd.common.exception.ClientException;
import com.github.ddd.common.exception.ErrorCodeEnum;
import com.github.ddd.common.exception.ServiceException;
import com.github.ddd.common.exception.SystemException;
import com.github.ddd.common.pojo.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常拦截
 *
 * @author ranger
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ServerResponse<?> handleBindException(HttpServletRequest request, BindException e) {
        StringBuilder message = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(error.getField()).append(":").append(error.getDefaultMessage()).append(";");
        }
        log.warn("请求路径 {}  参数绑定异常 {}", request.getRequestURI(), message);
        return ServerResponse.createErrorMsg(message.toString());
    }

    @ExceptionHandler(ClientException.class)
    @ResponseBody
    public ServerResponse<?> handleClientException(HttpServletRequest request, ClientException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StackTraceElement root = stackTrace[0];
        log.warn("请求路径 {}, 用户端异常: {}  对应代码 【{}】【{}】【{}】",
                request.getRequestURI(),
                e.getMessage(),
                root.getClassName(),
                root.getMethodName(),
                root.getLineNumber());
        return ServerResponse.createErrorMsg(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseBody
    public ServerResponse<?> handleSystemException(HttpServletRequest request, SystemException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StackTraceElement root = stackTrace[0];
        log.warn("请求路径 {}, 业务异常: {}  对应代码 【{}】【{}】【{}】",
                request.getRequestURI(),
                e.getMessage(),
                root.getClassName(),
                root.getMethodName(),
                root.getLineNumber());
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        response.setErrorMessage(e.getMessage());
        response.setShowType(ServerResponse.ShowType.ERROR);
        return response;
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ServerResponse<?> handleServiceException(HttpServletRequest request, ServiceException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StackTraceElement root = stackTrace[0];
        log.warn("请求路径 {}, 第三方服务异常: {}  对应代码 【{}】【{}】【{}】",
                request.getRequestURI(),
                e.getMessage(),
                root.getClassName(),
                root.getMethodName(),
                root.getLineNumber());
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.SERVICE_ERROR.getCode());
        response.setErrorMessage(e.getMessage());
        response.setShowType(ServerResponse.ShowType.ERROR);
        return response;
    }


    @ExceptionHandler(ClientAbortException.class)
    @ResponseBody
    public void handlerClientAbortException(HttpServletRequest request, ClientAbortException e) {
        log.warn("请求路径 {}, 客户端主动断开异常 {}", request.getRequestURI(), e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ServerResponse<?> handleNullPointerException(HttpServletRequest request, NullPointerException e) {
        log.error("请求路径 {}, 空引用异常: ", request.getRequestURI(), e);
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        response.setErrorMessage("空引用错误");
        response.setShowType(ServerResponse.ShowType.ERROR);
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse<?> handleLastException(HttpServletRequest request, Exception e) {
        log.error("请求路径 {}, 未知异常: ", request.getRequestURI(), e);
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        response.setErrorMessage("服务器未知错误");
        response.setShowType(ServerResponse.ShowType.ERROR);
        return response;
    }
}
