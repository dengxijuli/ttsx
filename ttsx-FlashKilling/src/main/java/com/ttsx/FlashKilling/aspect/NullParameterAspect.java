package com.ttsx.FlashKilling.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;

@Aspect
@Component
public class NullParameterAspect {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestParam)")
    public void requestParamMethods() {}

    @Around("requestParamMethods()")
    public Object handleNullParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        RequestParam requestMapping = method.getAnnotation(RequestParam.class);
        if (requestMapping != null && requestMapping.required()) {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg == null) {
                    // 处理参数为 null 的情况
                    String errorMessage = "参数不能为空";
                    // 返回适当的响应或执行其他操作
                    return errorMessage;
                }
            }
        }
        // 参数都不为 null，继续执行原方法
        return joinPoint.proceed();
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method currentMethod = methodSignature.getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        return targetClass.getMethod(currentMethod.getName(), currentMethod.getParameterTypes());
    }
}