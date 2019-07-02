package com.junzixiehui.wakaka.infrastructure.common.aspect;

import com.junzixiehui.wakaka.Application;
import com.junzixiehui.wakaka.infrastructure.common.utils.IPUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.junzixiehui.wakaka.Application;
import com.zhouyutong.zapplication.constant.SymbolConstant;
import com.zhouyutong.zapplication.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 日志跟踪
 * 本地trace日志格式：
 * 应用名称|IP(远程或本地)|kind调用类型|name调用方或接收方|param入参|result结果|duration耗时
 * kind说明：httpReceive|httpCall|dbCall|redisCall|esCall
 * <p>
 * 远程使用sleuth
 *
 * @author zhoutao
 * @date 2018/4/24
 */
@Component
@Slf4j
public class LogTracer {
    private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    @Value("${spring.sleuth.enabled}")
    private boolean sleuthEnabled;
    @Autowired
    private BeanFactory beanFactory;
    private org.springframework.cloud.sleuth.Tracer tracer;

    @PostConstruct
    public void postConstruct() {
        if (sleuthEnabled) {
            tracer = beanFactory.getBean(org.springframework.cloud.sleuth.Tracer.class);
        }
    }

    /**
     * 是否需要手工trace
     * httpReceive==>Controller由Sleuth的TraceWebAutoConfiguration自动装配
     * httpCall==>TestTemplate由Sleuth的TraceWebClientAutoConfiguration自动装配
     * jobCall==>定时任务不发送到远程
     *
     * @param traceContext
     * @return
     */
    private boolean isManualTrace(TraceContext traceContext) {
        String kind = traceContext.fetchKind();
        return !"httpReceive".equals(kind) && !"httpCall".equals(kind) && !"jobCall".equals(kind);
    }

    public void traceStart(TraceContext traceContext) {
        StringBuilder sb = traceContext.sb;
        sb.append(traceContext.fetchApplicationName()).append(SymbolConstant.BAR);
        sb.append(traceContext.fetchIp()).append(SymbolConstant.BAR);
        sb.append(traceContext.fetchKind()).append(SymbolConstant.BAR);
        sb.append(traceContext.fetchName()).append(SymbolConstant.BAR);
        sb.append(traceContext.fetchParam()).append(SymbolConstant.BAR);

        if (tracer != null) {
            Span currentSpan = null;
            if (isManualTrace(traceContext)) {
                currentSpan = tracer.createSpan(traceContext.fetchKind());
                currentSpan.tag(Span.SPAN_PEER_SERVICE_TAG_NAME, traceContext.fetchKind());
                currentSpan.logEvent(Span.CLIENT_SEND);
            } else {
                currentSpan = tracer.getCurrentSpan();
            }
            //附加关键参数
            Map<String, String> tagMap = traceContext.fetchPrimaryParam();
            if (tagMap != null && currentSpan != null) {
                for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                    currentSpan.tag(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void traceError(Throwable throwable, TraceContext traceContext) {
        String errorMsg = traceContext.fetchError(throwable);
        //打印本地异常日志
        errorLogger.error(errorMsg);

        //添加异常日志信息到远程(是否整个异常)
        if (tracer != null && tracer.getCurrentSpan() != null) {
            tracer.getCurrentSpan().tag(Span.SPAN_ERROR_TAG_NAME, errorMsg);
        }
    }

    public void traceEnd(TraceContext traceContext) {
        StringBuilder sb = traceContext.sb;
        sb.append(traceContext.fetchResult()).append(SymbolConstant.BAR);
        sb.append(traceContext.fetchDuration()).append(SymbolConstant.BAR);
        log.info(sb.toString());

        if (tracer != null && tracer.getCurrentSpan() != null) {
            //附加关键结果
            Map<String, String> tagMap = traceContext.fetchPrimaryResult();
            if (tagMap != null) {
                for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                    tracer.getCurrentSpan().tag(entry.getKey(), entry.getValue());
                }
            }

            if (isManualTrace(traceContext)) {
                tracer.getCurrentSpan().logEvent(Span.CLIENT_RECV);
                tracer.close(tracer.getCurrentSpan());
            }
        }

        traceContext.clear();
    }

    public abstract static class TraceContext {
        private Joiner joiner = Joiner.on(SymbolConstant.COMMA);
        private long start = System.currentTimeMillis();
        private StringBuilder sb = new StringBuilder();
        protected AspectInfo aspectInfo;

        protected TraceContext(AspectInfo aspectInfo) {
            this.aspectInfo = aspectInfo;
        }

        private void clear() {
            this.sb.setLength(0);
        }

        public String currentContextString() {
            return sb.toString();
        }

        protected void setResult(Object result) {
            aspectInfo.setMethodReturnValue(result);
        }

        protected String fetchApplicationName() {
            return Application.NAME;
        }

        protected String fetchIp() {
            return IPUtils.LOCAL_IP;
        }

        abstract protected String fetchKind();

        abstract protected String fetchName();

        /**
         * 异常信息
         *
         * @return
         */
        protected String fetchError(Throwable throwable) {
            StringBuilder errorSB = new StringBuilder("Error");
            //谁发生了异常
            errorSB.append(SymbolConstant.COMMA).append(fetchName());
            //关键的参数是什么比如userId等
            errorSB.append(SymbolConstant.COMMA).append(fetchPrimaryParam() == null ? SymbolConstant.EMPTY : fetchPrimaryParam());
            //发生了什么异常
            if (throwable instanceof ServiceException) {
                errorSB.append(SymbolConstant.COMMA).append(throwable.toString());
            } else {
                errorSB.append(SymbolConstant.COMMA).append(Throwables.getStackTraceAsString(throwable));
            }
            return errorSB.toString();
        }

        /**
         * 关键参数
         *
         * @return
         */
        protected Map<String, String> fetchPrimaryParam() {
            return null;
        }

        /**
         * 关键结果
         *
         * @return
         */
        protected Map<String, String> fetchPrimaryResult() {
            return null;
        }

        protected String fetchParam() {
            Object[] params = aspectInfo.getMethodParam();
            if (ArrayUtils.isEmpty(params)) {
                return SymbolConstant.EMPTY;
            }

            StringBuilder sb = new StringBuilder();
            List<Object> paramList = Lists.newArrayListWithExpectedSize(params.length);
            for (Object param : params) {
                if (param == null || param instanceof BindingResult
                        || param instanceof ServletRequest) {
                    continue;
                }
                paramList.add(param.toString());
            }

            joiner.appendTo(sb, paramList);
            return sb.toString();
        }

        protected String fetchResult() {
            return aspectInfo.getMethodReturnValue() == null ? SymbolConstant.EMPTY : aspectInfo.getMethodReturnValue().toString();
        }

        protected String fetchDuration() {
            long end = System.currentTimeMillis();
            return Long.toString(end - start);
        }
    }
}