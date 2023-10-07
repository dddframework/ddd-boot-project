package com.github.ddd.mybatis.exception;

import com.github.ddd.common.pojo.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author ranger
 */
@Slf4j
@ControllerAdvice
public class DaoExceptionHandler {

    /**
     * 处理重复异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public ServerResponse<?> handlerDuplicateKeyException(HttpServletRequest request, DuplicateKeyException e) {
        log.error("请求路径 {}, 重复数据异常: {}", request.getRequestURI(), e);
        return ServerResponse.createErrorMsg("存在重复数据");
    }


}
