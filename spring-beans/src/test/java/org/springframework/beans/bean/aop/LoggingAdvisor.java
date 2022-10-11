package org.springframework.beans.bean.aop;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20229/16
 */
public class LoggingAdvisor implements AfterReturningAdvice /*, MethodBeforeAdvice*/ {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("====== AOP Advisor：The method " + method.getName() + " End " + " ============");
    }

    /*@Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("====== AOP Advisor：The method " + method.getName() + " begins with " + args + " ============");
    }*/
}
