package com.github.ddd.web.exception;

import com.github.ddd.common.exception.ClientException;
import com.github.ddd.common.exception.ErrorCodeEnum;
import com.github.ddd.common.exception.ServiceException;
import com.github.ddd.common.exception.SystemException;
import com.github.ddd.common.pojo.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        log.warn("请求路径 {}  用户端参数异常", request.getRequestURI());
        StringBuilder message = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            message.append(error.getField()).append(":").append(error.getDefaultMessage()).append(";");
        }
        return ServerResponse.createErrorMsg(message.toString());
    }

    @ExceptionHandler(ClientException.class)
    @ResponseBody
    public ServerResponse<?> handleClientException(HttpServletRequest request, ClientException e) {
        log.warn("请求路径 {}, 用户端异常: {} ", request.getRequestURI(), e.getMessage());
        return ServerResponse.createErrorMsg(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseBody
    public ServerResponse<?> handleSystemException(HttpServletRequest request, SystemException e) {
        log.warn("请求路径 {}, 系统业务异常: {} ", request.getRequestURI(), e.getMessage());
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
        log.error("请求路径 {}, 第三方服务异常: ", request.getRequestURI(), e);
        ServerResponse<?> response = new ServerResponse<>();
        response.setSuccess(false);
        response.setErrorCode(ErrorCodeEnum.SERVICE_ERROR.getCode());
        response.setErrorMessage(e.getMessage());
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
