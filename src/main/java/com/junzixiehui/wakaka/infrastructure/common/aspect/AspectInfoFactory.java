package com.junzixiehui.wakaka.infrastructure.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author zhoutao
 * @date 2018/12/29
 */
public class AspectInfoFactory {
    public static AspectInfo create(ProceedingJoinPoint pjp) {
        //方法参数
        Object[] methodParams = pjp.getArgs();
        //方法名
        String methodName = pjp.getSignature().getName();
        //切面类
        Class aspectClass = pjp.getTarget().getClass();
        AspectInfo aspectInfo = new AspectInfo();
        aspectInfo.setMethodParam(methodParams);
        aspectInfo.setMethodName(methodName);
        aspectInfo.setClassType(aspectClass);
        return aspectInfo;
    }
}
