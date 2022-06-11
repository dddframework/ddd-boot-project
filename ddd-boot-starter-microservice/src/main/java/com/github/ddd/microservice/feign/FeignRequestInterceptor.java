package com.github.ddd.microservice.feign;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用于Feign传递请求头的拦截器
 *
 * @author ranger
 */
@RequiredArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {

    private final List<String> headers;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        if (headers != null) {
            for (String name : headers) {
                String values = request.getHeader(name);
                if (StrUtil.isNotBlank(values)) {
                    requestTemplate.header(name, values);
                }
            }
        }
    }
}