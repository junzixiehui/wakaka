package com.junzixiehui.wakaka.infrastructure.common.aspect;

import com.google.common.collect.Lists;
import com.zhouyutong.zapplication.api.ErrorCode;
import com.zhouyutong.zapplication.api.Resp;
import com.zhouyutong.zapplication.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接受请求时打印日志
 * 应用名称|本机IP,远程IP|httpReceive|controllerName.methodName|入参|结果|耗时毫秒
 *
 * @author zhoutao
 * @Description: 1.对接口做拦截打印调用日志<br>
 * 2.统一异常处理打印异常日志<br>
 * @date 2018/3/8
 */
@Aspect
@Component
@Slf4j
public class ControllerAspect {
    @Autowired
    private LogTracer logTracer;

    @Around("execution(public * com.junzixiehui.wakaka.web..*Controller.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        AspectInfo aspectInfo = AspectInfoFactory.create(pjp);
        HttpReceiveTraceContext context = new HttpReceiveTraceContext(aspectInfo);
        //请求接收先打印一个开始
        log.info(context.fetchName() + " start...");
        logTracer.traceStart(context);

        Object result = null;
        try {
            /**
             * jsr注解参数校验结果获取部分
             * 校验不通过直接返回结果,不走业务逻辑
             */
            Resp resp = ControllerAspectHelper.checkParamValidateResult(pjp.getArgs());

            /**
             * 业务逻辑调用部分
             */
            if (resp.hasSuccess()) {
                result = pjp.proceed();
            } else {
                result = resp;
            }
        } catch (Throwable ex) {
            Class controllerMethodReturnClass = ControllerAspectHelper.getReturnClass(aspectInfo);
            if (Resp.class == controllerMethodReturnClass) {
                result = Resp.error(ex);
            } else if (String.class == controllerMethodReturnClass) {
                result = Resp.error(ex).toSimpleString();
            } else {
                result = ex;
            }
            logTracer.traceError(ex, context);
        } finally {
            context.setResult(result);
            logTracer.traceEnd(context);
        }
        return result;
    }

    private static class ControllerAspectHelper {
        static final List<String> PARAM_REQUIRED_CODE_LIST = Lists.newArrayList(
                NotBlank.class.getSimpleName(),
                NotEmpty.class.getSimpleName(),
                NotNull.class.getSimpleName()
        );

        //获取返回值类型
        static Class getReturnClass(AspectInfo aspectInfo) {
            String methodName = aspectInfo.getMethodName();
            Method[] methods = aspectInfo.getClassType().getDeclaredMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    return method.getReturnType();
                }
            }
            return null;
        }

        /**
         * 参数验证结果检查
         *
         * @param params
         * @throws ServiceException
         */
        static Resp checkParamValidateResult(Object[] params) throws ServiceException {
            if (ArrayUtils.isEmpty(params)) {
                return Resp.success();
            }

            //从参数列表中提取BindingResult
            BindingResult bindingResult = null;
            for (Object param : params) {
                if (param instanceof BindingResult) {
                    bindingResult = (BindingResult) param;
                    break;
                }
            }

            if (bindingResult != null && bindingResult.hasErrors()) {
                List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
                for (FieldError fieldError : fieldErrorList) {
                    if (PARAM_REQUIRED_CODE_LIST.contains(fieldError.getCode())) {
                        String code = ErrorCode.PARAM_REQUIRED.getCode();
                        String message = String.format(ErrorCode.PARAM_REQUIRED.getMessage(), fieldError.getField());
                        return Resp.error(code, message);
                    } else {
                        String code = ErrorCode.PARAM_INVALID.getCode();
                        String message = String.format(ErrorCode.PARAM_INVALID.getMessage(), fieldError.getField());
                        return Resp.error(code, message);
                    }
                }
            }

            return Resp.success();
        }
    }

    private static class HttpReceiveTraceContext extends LogTracer.TraceContext {

        public HttpReceiveTraceContext(AspectInfo aspectInfo) {
            super(aspectInfo);
        }

        @Override
        public Map<String, String> fetchPrimaryResult() {
            Map<String, String> tag = new HashMap<String, String>();
            Object methodReturnValue = super.aspectInfo.getMethodReturnValue();
            if (methodReturnValue instanceof Resp) {
                Resp resp = (Resp) methodReturnValue;
                tag.put("resp.code", resp.getCode());
                tag.put("resp.msg", resp.getMessage());
            } else {
                tag.put("resp.code", "200");
                tag.put("resp.msg", methodReturnValue.toString());
            }
            return tag;
        }

        @Override
        protected String fetchKind() {
            return "httpReceive";
        }

        @Override
        protected String fetchName() {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return httpServletRequest.getRequestURI();
        }
    }
}
