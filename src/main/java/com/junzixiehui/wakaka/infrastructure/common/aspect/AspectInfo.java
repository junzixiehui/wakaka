package com.junzixiehui.wakaka.infrastructure.common.aspect;

import lombok.Data;

/**
 * 切面信息
 *
 * @author zhoutao
 * @date 2018/12/29
 */
@Data
public class AspectInfo {
    private Object[] methodParam;
    private String methodName;
    private Class methodReturnClassType;
    private Object methodReturnValue;
    private Class classType;
}
