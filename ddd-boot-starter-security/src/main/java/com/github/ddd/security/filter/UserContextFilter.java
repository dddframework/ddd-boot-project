package com.github.ddd.security.filter;

import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.pojo.UserDetail;
import com.github.ddd.common.util.UserContextHolder;
import com.github.ddd.security.core.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户上下拦截
 *
 * @author ranger
 */
@RequiredArgsConstructor
public class UserContextFilter extends OncePerRequestFilter {

    private final SessionManager sessionManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
        UserDetail currentUser = parseUserHeader(sessionId);
        if (currentUser != null) {
            UserContextHolder.setUserContext(currentUser);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 解析UserDetail
     *
     * @param sessionId
     * @return UserDetail
     */
    public UserDetail parseUserHeader(String sessionId) {
        if (StrUtil.isBlank(sessionId)) {
            return null;
        }
        return sessionManager.getUserDetailBySessionId(sessionId);
    }

}
