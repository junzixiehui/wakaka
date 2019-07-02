package com.junzixiehui.wakaka.infrastructure.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * job运行过程打印日志切面
 * 应用名称|本机IP|jobCall|jobClassName|shardingContext|end.|耗时毫秒
 *
 * @date 2018/3/8
 */
@Aspect
@Component
@Slf4j
public class JobAspect {
    @Autowired
    private LogTracer logTracer;

    @Around("execution(public * com.junzixiehui.wakaka.web.job..*Job.execute(..))")
    public void aroundSimpleJob(ProceedingJoinPoint pjp) throws Throwable {
        AspectInfo aspectInfo = AspectInfoFactory.create(pjp);
        JobTraceContext context = new JobTraceContext(aspectInfo);
        //多打一行job开始,看日志更方便
        log.info(context.fetchName() + " start...");
        logTracer.traceStart(context);
        try {
            pjp.proceed();
        } catch (Throwable ex) {
            logTracer.traceError(ex, context);
        } finally {
            logTracer.traceEnd(context);
        }
    }

    private static class JobTraceContext extends LogTracer.TraceContext {

        public JobTraceContext(AspectInfo aspectInfo) {
            super(aspectInfo);
        }

        @Override
        protected String fetchResult() {
            return "end.";
        }

        @Override
        protected String fetchKind() {
            return "jobCall";
        }

        @Override
        protected String fetchName() {
            return super.aspectInfo.getClassType().getSimpleName();
        }
    }
}
