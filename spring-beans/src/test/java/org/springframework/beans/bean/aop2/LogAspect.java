package org.springframework.beans.bean.aop2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

import static org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving.ENABLED;

@Aspect
@EnableLoadTimeWeaving(             // 开启 LTW 功能
        /*
         * aspectjWeaving 指定 LTW 的开启策略：
         *      ENABLED：开启，DISABLED：不开启，
         *      AUTODETECT：如果类路径下能读取到 META-INF/aop.xml，则开启，否则关闭
         */
        aspectjWeaving = ENABLED
)
@Configuration
public class LogAspect {

    @Pointcut("execution(* org.springframework.beans.bean.aop2.LtwBean.*(..))")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public void advise(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        System.out.println(signature + " Start2 ...");
        pjp.proceed();
        System.out.println(signature + " End ...");
    }
}
