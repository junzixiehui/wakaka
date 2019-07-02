package com.junzixiehui.wakaka.infrastructure.common.aspect;

import com.zhouyutong.zapplication.constant.SymbolConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 拦截dao
 * 应用名称|本机IP|dbCall或esCall|daoClassName.daoMethodName|EMPTY_STRING|EMPTY_STRING|耗时毫秒
 *
 * @author zhoutao
 * @date 2018/3/8
 */
@Aspect
@Component
public class DaoAspect {
    @Autowired
    private LogTracer logTracer;

    @Around("execution(public * com.junzixiehui.wakaka.infrastructure.dao..*Dao.*(..))")
    private Object daoAround(ProceedingJoinPoint pjp) throws Throwable {
        return this.around(pjp);
    }

    @Around("execution(public * com.zhouyutong.zorm.dao.AbstractBaseDao.*(..))")
    private Object baseDaoAround(ProceedingJoinPoint pjp) throws Throwable {
        return this.around(pjp);
    }

    private Object around(ProceedingJoinPoint pjp) throws Throwable {
        DaoTraceContext daoTraceContext = new DaoTraceContext(AspectInfoFactory.create(pjp));
        logTracer.traceStart(daoTraceContext);
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable ex) {
            logTracer.traceError(ex, daoTraceContext);
            throw ex;
        } finally {
            daoTraceContext.setResult(result);
            logTracer.traceEnd(daoTraceContext);
        }
        return result;
    }

    private static class DaoTraceContext extends LogTracer.TraceContext {

        public DaoTraceContext(AspectInfo aspectInfo) {
            super(aspectInfo);
        }

        @Override
        public Map<String, String> fetchPrimaryParam() {
            Map<String, String> tag = new HashMap<String, String>();
            tag.put("db.op", fetchName());
            return tag;
        }

        @Override
        protected String fetchKind() {
            String daoSuperName = super.aspectInfo.getClassType().getSuperclass().getSimpleName();
            if (daoSuperName.equals("JdbcBaseDao")) {
                return "dbCall";
            } else if (daoSuperName.equals("ElasticSearchBaseDao")) {
                return "esCall";
            } else if (daoSuperName.equals("CassandraBaseDao")) {
                return "cassandraCall";
            }
            return SymbolConstant.EMPTY;
        }

        @Override
        protected String fetchName() {
            String daoName = super.aspectInfo.getClassType().getSuperclass().getSimpleName();
            String daoMethodName = super.aspectInfo.getMethodName();
            StringBuilder caller = new StringBuilder();
            caller.append(daoName).append(".").append(daoMethodName);
            return caller.toString();
        }
    }
}
