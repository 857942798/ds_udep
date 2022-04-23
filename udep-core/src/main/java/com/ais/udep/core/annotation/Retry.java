package com.ais.udep.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/22
 * @Description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    //发生指定异常时重试
    Class[] exceptionClass() default {};

    //出现Exception时重试
    boolean retryIfException() default false;

    //程序出现RuntimeException异常时重试
    boolean retryIfRuntimeException() default false;

    //重试次数
    int retryNumber() default 3;

    //重试间隔 ms
    long waitStrategySleepTime() default 1000;

    //持续时间 ms
    long duration() default 30000;

    //返回值为false时重试（默认关闭）  不支持同时设置指定返回字符串重试
    boolean closeReturnFalseRetry() default true;
}
