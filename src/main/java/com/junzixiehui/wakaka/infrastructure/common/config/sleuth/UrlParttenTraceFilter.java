package com.junzixiehui.wakaka.infrastructure.common.config.sleuth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.web.TraceFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 接收Http请求需要过滤那些不需要链路跟踪的请求
 * 内置的TraceFilter即使匹配了需要skip的地址也会有一堆不需要的逻辑
 *
 * @author zhoutao
 * @date 2019/4/12
 */
@Slf4j
public class UrlParttenTraceFilter extends TraceFilter {
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();
    private Pattern skipPattern;

    public UrlParttenTraceFilter(BeanFactory beanFactory, Pattern skipPattern) {
        super(beanFactory, skipPattern);
        this.skipPattern = skipPattern;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("Filter just supports HTTP requests");
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = this.urlPathHelper.getPathWithinApplication(request);
        boolean skip = this.skipPattern.matcher(uri).matches();
        if (skip) {
            if (log.isDebugEnabled()) {
                log.debug("The receive url {} will not track.", uri);
            }
            filterChain.doFilter(request, response);
        } else {
            super.doFilter(servletRequest, servletResponse, filterChain);
        }
    }
}
