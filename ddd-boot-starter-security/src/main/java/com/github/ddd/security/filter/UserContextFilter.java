package com.github.ddd.security.filter;

import cn.hutool.core.util.StrUtil;
import com.github.ddd.security.core.SecurityService;
import com.github.ddd.security.pojo.UserDetail;
import com.github.ddd.security.util.SecurityUtil;
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

    private final SecurityService securityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserDetail currentUser = parseUserHeader(request);
        if (currentUser != null) {
            SecurityUtil.setUserContext(currentUser);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 解析UserDetail
     *
     * @param
     * @return
     */
    public UserDetail parseUserHeader(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(token)) {
            return null;
        }
        return securityService.parseToken(token);
    }

}
