package org.springframework.beans.bean.aop2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.stereotype.Component;

import static org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving.AUTODETECT;
import static org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving.DISABLED;
import static org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving.ENABLED;

@Aspect
public class LogAspect {

    @Pointcut("execution(* org.springframework.beans.bean.aop2.LtwBean.*(..))")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public void advise(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        System.out.println("\n" + signature + " Start2 ...");
        pjp.proceed();
        System.out.println(signature + " End ... \n");
    }
}
