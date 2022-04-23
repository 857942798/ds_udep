package com.ais.udep.core.retry;

import com.ais.udep.core.annotation.Retry;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/22
 * @Description:
 */
@Aspect
@Component
public class RetryAspect {
    @Pointcut("@annotation(com.ais.udep.core.annotation.Retry)")
    public void MethodCallConstraintPointcut(){
    }

    @Around(value = "@annotation(com.ais.udep.core.annotation.Retry)")
    public Object monitorAround(ProceedingJoinPoint pjp) throws Throwable {
        Method method;
        if (pjp.getSignature() instanceof MethodSignature) {  //判断注解是否method 上
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            method = signature.getMethod();
        } else {
            return null;
        }

        Retry annotation = method.getDeclaredAnnotation(Retry.class);
        //重试时间，重试次数
        if (annotation.duration() <= 0 && annotation.retryNumber() <= 1) {
            return pjp.proceed();
        }

        RetryerBuilder builder = RetryerBuilder.newBuilder();

        //重试次数
        if (annotation.retryNumber() > 0) {
            builder.withStopStrategy(StopStrategies.stopAfterAttempt(annotation.retryNumber()));
        }
        //退出策略
        if (annotation.duration() > 0) {
            builder.withStopStrategy(StopStrategies.stopAfterDelay(annotation.duration(), TimeUnit.MILLISECONDS));
        }

        //重试间间隔等待策略
        if (annotation.waitStrategySleepTime() > 0) {
            builder.withWaitStrategy(WaitStrategies.fixedWait(annotation.waitStrategySleepTime(), TimeUnit.MILLISECONDS));
        }

        //发生指定异常时重试
        if (annotation.exceptionClass().length > 0) {
            for (Class retryThrowable : annotation.exceptionClass()) {
                if (retryThrowable != null && Throwable.class.isAssignableFrom(retryThrowable)) {
                    builder.retryIfExceptionOfType(retryThrowable);
                }
            }
        }

        //RuntimeException时重试
        if (annotation.retryIfRuntimeException()){
            builder.retryIfRuntimeException();
        }

        if (annotation.retryIfException()){
            builder.retryIfException();
        }

        if (!annotation.closeReturnFalseRetry()){
            builder.retryIfResult(Predicates.equalTo(annotation.closeReturnFalseRetry()));
        }

        return builder.build().call(() -> {
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new Exception(throwable);
            }
        });
    }
}
