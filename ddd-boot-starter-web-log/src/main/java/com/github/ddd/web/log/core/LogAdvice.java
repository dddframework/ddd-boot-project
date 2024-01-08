package com.github.ddd.web.log.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.github.ddd.common.pojo.UserDetail;
import com.github.ddd.common.util.JacksonUtil;
import com.github.ddd.common.util.UserContextHolder;
import com.github.ddd.tenant.core.TenantDbHandler;
import com.github.ddd.tinyid.core.TinyIdUtil;
import com.github.ddd.web.log.spring.boot.autoconfigure.WebLogProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ranger
 */
@Slf4j
@RequiredArgsConstructor
public class LogAdvice implements MethodInterceptor {


    /**
     * 使用的线程池
     */
    private final static ThreadPoolExecutor THREAD_POOL;

    static {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        THREAD_POOL = new ThreadPoolExecutor(availableProcessors, availableProcessors, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    }

    private final JdbcTemplate jdbcTemplate;
    private final WebLogProperties webLogProperties;
    private final TenantDbHandler tenantDbHandler;

    private final String sql = "INSERT INTO %s(`id`, `name`, `user_id`, `nickname`, `request_uri`, `user_agent`, `client_ip`, `result`, `time`, `create_time`, `request_params`, `exception`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public Object invoke(MethodInvocation joinPoint) throws Throwable {
        Method method = joinPoint.getMethod();
        BizLog bizlog = AnnotationUtils.findAnnotation(method, BizLog.class);
        if (bizlog == null) {
            return joinPoint.proceed();
        }
        // 记录日志
        SysLog sysLog = new SysLog();
        sysLog.setId(TinyIdUtil.nextId("LOG"));
        long start = System.currentTimeMillis();
        sysLog.setCreateTime(new Date());
        try {
            sysLog.setResult(SysLog.OK);
            return joinPoint.proceed();
        } catch (Throwable e) {
            sysLog.setResult(SysLog.FAIL);
            StackTraceElement[] stackTrace = e.getStackTrace();
            StackTraceElement root = stackTrace[0];
            String error = StrUtil.format("{} :对应代码 【{}】【{}】【{}】",
                    e.getMessage(),
                    root.getClassName(),
                    root.getMethodName(),
                    root.getLineNumber());
            sysLog.setException(error);
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            sysLog.setTime(end - start);
            buildReqLog(sysLog, joinPoint);
            UserDetail currentUser = UserContextHolder.getCurrentUser();
            if (currentUser != null) {
                sysLog.setUserId(currentUser.getUserId());
                sysLog.setNickname(currentUser.getNickname());
            } else {
                sysLog.setUserId(-1L);
                sysLog.setNickname("未登录用户");
            }
            THREAD_POOL.submit(() -> {
                try {
                    UserContextHolder.setUserContext(currentUser);
                    jdbcTemplate.update(String.format(sql, tenantDbHandler.parseTrueTableName(webLogProperties.getBizLogTable())),
                            sysLog.getId(),
                            sysLog.getName(),
                            sysLog.getUserId(),
                            sysLog.getNickname(),
                            sysLog.getRequestUri(),
                            sysLog.getUserAgent(),
                            sysLog.getClientIp(),
                            sysLog.getResult(),
                            sysLog.getTime(),
                            sysLog.getCreateTime(),
                            sysLog.getRequestParams(),
                            sysLog.getException());
                } catch (Exception e) {
                    log.warn("日志记录失败 路径：{} 异常 {}", sysLog.getRequestUri(), e.getMessage());
                }
            });
        }
    }

    private void buildReqLog(SysLog log, MethodInvocation joinPoint) {
        Method method = joinPoint.getMethod();
        Object[] args = joinPoint.getArguments();
        BizLog bizlog = AnnotationUtils.findAnnotation(method, BizLog.class);
        StringBuilder builder = new StringBuilder(StrUtil.EMPTY);
        assert bizlog != null;
        if (bizlog.recordParam()) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof MultipartFile || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                    continue;
                }
                builder.append("arg[").append(i).append("]:").append(JacksonUtil.toJsonStr(arg)).append("\n");
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String bizName = bizlog.value();
        log.setRequestUri(request.getRequestURI());
        log.setRequestParams(builder.toString());
        log.setClientIp(ServletUtil.getClientIP(request));
        log.setUserAgent(ServletUtil.getHeaderIgnoreCase(request, "User-Agent"));
        log.setName(bizName);
    }
}
