package com.junzixiehui.wakaka.infrastructure.common.aspect;

import com.zhouyutong.zapplication.constant.SymbolConstant;
import com.zhouyutong.zapplication.exception.RedisCallException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 拦截RedisTemplateWarpper
 * 访问redis前检查key、打印redis访问日志
 * 应用名称|本机IP|redisCall|methodName|key名称|结果|耗时毫秒
 *
 * @author zhoutao
 * @date 2018/3/8
 */
@Aspect
@Component
@Slf4j
public class RedisTemplateAspect {
    @Autowired
    private LogTracer logTracer;

    @Around("execution(public * com.junzixiehui.wakaka.infrastructure.common.config.redis.RedisTemplateWarpper.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        AspectInfo aspectInfo = AspectInfoFactory.create(pjp);
        RedisTraceContext redisTraceContext = new RedisTraceContext(aspectInfo);
        logTracer.traceStart(redisTraceContext);

        Object result = null;
        try {
            result = pjp.proceed();
        } catch (IllegalArgumentException ex) {
            logTracer.traceError(ex, redisTraceContext);
            throw ex;
        } catch (Throwable ex) {
            logTracer.traceError(ex, redisTraceContext);
            throw new RedisCallException(ex.getMessage(), ex);
        } finally {
            redisTraceContext.setResult(result);
            logTracer.traceEnd(redisTraceContext);
        }
        return result;
    }

    private static class RedisTraceContext extends LogTracer.TraceContext {

        public RedisTraceContext(AspectInfo aspectInfo) {
            super(aspectInfo);
        }

        public String getKey() {
            Object[] params = super.aspectInfo.getMethodParam();
            if (params == null || params.length == 0) {
                return SymbolConstant.EMPTY;
            }
            return params[0] == null ? SymbolConstant.EMPTY : params[0].toString();
        }

        @Override
        public Map<String, String> fetchPrimaryParam() {
            Map<String, String> tag = new HashMap<String, String>();
            tag.put("redis.op", fetchName());
            tag.put("redis.key", getKey());
            return tag;
        }

        @Override
        protected String fetchKind() {
            return "redisCall";
        }

        @Override
        protected String fetchName() {
            String redisOp = super.aspectInfo.getMethodName();
            return redisOp;
        }
    }
}