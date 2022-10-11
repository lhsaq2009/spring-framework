package org.springframework.beans.bean.aop;

import org.aspectj.lang.JoinPoint;

import java.util.Arrays;
import java.util.List;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20229/16
 */
public class LoggingAspect {

    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("====== AOP Aspect：The method " + methodName + " begins with " + args + " ============");
    }

    public void afterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("====== AOP Aspect afterMethod");
    }
}
