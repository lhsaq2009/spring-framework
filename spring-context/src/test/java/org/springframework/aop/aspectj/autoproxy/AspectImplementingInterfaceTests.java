package org.springframework.aop.aspectj.autoproxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;

import org.springframework.aop.framework.Advised;
import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for ensuring the aspects aren't advised. See SPR-3893 for more details.
 *
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public class AspectImplementingInterfaceTests {

	@Test
	public void testProxyCreation() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());

		ITestBean testBean = (ITestBean) ctx.getBean("testBean");
		AnInterface interfaceExtendingAspect = (AnInterface) ctx.getBean("interfaceExtendingAspect");

		boolean condition = testBean instanceof Advised;
		assertThat(condition).isTrue();
		boolean condition1 = interfaceExtendingAspect instanceof Advised;
		assertThat(condition1).isFalse();
	}

}


interface AnInterface {
	public void interfaceMethod();
}


class InterfaceExtendingAspect implements AnInterface {
	public void increment(ProceedingJoinPoint pjp) throws Throwable {
		pjp.proceed();
	}

	@Override
	public void interfaceMethod() {
	}
}
