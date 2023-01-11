package org.springframework.aop.aspectj;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class AspectJExpressionPointcutAdvisorTests {

	private ITestBean testBean;

	private CallCountingInterceptor interceptor;


	@BeforeEach
	public void setup() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		testBean = (ITestBean) ctx.getBean("testBean");
		interceptor = (CallCountingInterceptor) ctx.getBean("interceptor");
	}


	@Test
	public void testPointcutting() {
		assertThat(interceptor.getCount()).as("Count should be 0").isEqualTo(0);
		testBean.getSpouses();
		assertThat(interceptor.getCount()).as("Count should be 1").isEqualTo(1);
		testBean.getSpouse();
		assertThat(interceptor.getCount()).as("Count should be 1").isEqualTo(1);
	}

}


class CallCountingInterceptor implements MethodInterceptor {

	private int count;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		count++;
		return methodInvocation.proceed();
	}

	public int getCount() {
		return count;
	}

	public void reset() {
		this.count = 0;
	}
}
