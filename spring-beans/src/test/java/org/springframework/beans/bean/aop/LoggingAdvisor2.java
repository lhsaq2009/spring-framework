package org.springframework.beans.bean.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
// 不能被单独扫描，<aop:aspectj-autoproxy 无效，<context:component-scan 无效，<context:annotation-config/> 也无效，得配合 @Component
public class LoggingAdvisor2 {

    @Before("execution(* org.springframework.beans.bean.aop.ArithmeticCalculator.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("=========== LoggingAdvisor2 Before 111111 ===============");
    }
}
