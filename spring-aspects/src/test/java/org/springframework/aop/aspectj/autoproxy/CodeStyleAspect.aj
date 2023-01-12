package org.springframework.aop.aspectj.autoproxy;

import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * @author Adrian Colyer
 */
public aspect CodeStyleAspect {

	@SuppressWarnings("unused")
	private String foo;

	pointcut somePC() : call(* someMethod());

	@SuppressAjWarnings("adviceDidNotMatch")
	before() : somePC() {
		System.out.println("match");
	}

	public void setFoo(String foo) {
		this.foo = foo;
	}

}
