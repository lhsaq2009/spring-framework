package org.springframework.beans.model;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20239/19
 */
//@EnableAspectJAutoProxy
//@Aspect
//@Component
public class AspectWomen {

	@Before("execution(public void org.springframework.beans.model.Women.say())")
	public void say() {
		System.out.println("AspectMan -> Women -> say() ------------------------");
	}
}
