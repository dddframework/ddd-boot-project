package com.github.ddd.security.filter;

import com.github.ddd.security.annotation.CheckPermission;
import com.github.ddd.security.annotation.NotLogin;
import com.github.ddd.security.pojo.UserDetail;
import com.github.ddd.security.util.SecurityUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 权限注解拦截
 *
 * @author ranger
 */
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        // 不需要登录
        NotLogin notLogin = AnnotationUtils.findAnnotation(method, NotLogin.class);
        if (notLogin != null) {
            return true;
        }
        // 需要登录
        UserDetail currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            this.outputError(response, HttpStatus.UNAUTHORIZED);
            return false;
        }
        CheckPermission checkPermission = AnnotationUtils.findAnnotation(method, CheckPermission.class);
        if (checkPermission == null) {
            return true;
        }
        // 需要权限
        boolean and = checkPermission.and();
        String[] value = checkPermission.value();
        Set<String> codes = currentUser.getAuthCodes();
        if (codes != null) {
            if (and) {
                for (String v : value) {
                    if (!codes.contains(v)) {
                        this.outputError(response, HttpStatus.FORBIDDEN);
                        return false;
                    }
                }
                return true;
            } else {
                for (String v : value) {
                    if (codes.contains(v)) {
                        return true;
                    }
                }
                return false;
            }
        }
        this.outputError(response, HttpStatus.FORBIDDEN);
        return false;
    }

    /**
     * 输出错误信息
     *
     * @param response
     * @throws IOException
     */
    private void outputError(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
        response.reset();
        response.setStatus(httpStatus.value());
        PrintWriter out = response.getWriter();
        out.print(httpStatus.getReasonPhrase());
        out.flush();
        out.close();
    }
}
