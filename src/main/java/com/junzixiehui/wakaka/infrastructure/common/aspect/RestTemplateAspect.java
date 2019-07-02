package com.junzixiehui.wakaka.infrastructure.common.aspect;

import com.zhouyutong.zapplication.constant.SymbolConstant;
import com.zhouyutong.zapplication.exception.HttpCallException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拦截restTemplate调用远程请求打印trace日志
 * 应用名称|本机IP|httpCall|远程url|入参|结果|耗时毫秒
 *
 * @author zhoutao
 * @date 2018/3/8
 */
@Aspect
@Component
@Slf4j
public class RestTemplateAspect {
    @Autowired
    private LogTracer logTracer;

    @Around("execution(public * org.springframework.web.client.RestTemplate.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        AspectInfo aspectInfo = AspectInfoFactory.create(pjp);
        HttpCallTraceContext traceContext = new HttpCallTraceContext(aspectInfo);
        logTracer.traceStart(traceContext);

        Object result = null;
        try {
            result = pjp.proceed();
        } catch (IllegalArgumentException ex) {
            logTracer.traceError(ex, traceContext);
            throw ex;
        } catch (Throwable ex) {
            logTracer.traceError(ex, traceContext);
            throw new HttpCallException(ex.getMessage(), ex);
        } finally {
            traceContext.setResult(result);
            logTracer.traceEnd(traceContext);
        }
        return result;
    }

    private static class HttpCallTraceContext extends LogTracer.TraceContext {

        public HttpCallTraceContext(AspectInfo aspectInfo) {
            super(aspectInfo);
        }

        @Override
        protected String fetchKind() {
            return "httpCall";
        }

        @Override
        protected String fetchName() {
            Object[] params = super.aspectInfo.getMethodParam();
            if (params == null || params.length == 0) {
                return super.aspectInfo.getMethodName();
            }
            String url = params[0] == null ? SymbolConstant.EMPTY : params[0].toString();
            int n = url.indexOf("?");
            if (n != -1) {
                url = url.substring(0, n - 1);
            }
            return url;
        }
    }
}
