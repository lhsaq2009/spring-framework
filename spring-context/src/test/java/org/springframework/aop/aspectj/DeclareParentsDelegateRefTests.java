package org.springframework.aop.aspectj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public class DeclareParentsDelegateRefTests {

	protected NoMethodsBean noMethodsBean;

	protected Counter counter;


	@BeforeEach
	public void setup() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		noMethodsBean = (NoMethodsBean) ctx.getBean("noMethodsBean");
		counter = (Counter) ctx.getBean("counter");
		counter.reset();
	}


	@Test
	public void testIntroductionWasMade() {
		boolean condition = noMethodsBean instanceof ICounter;
		assertThat(condition).as("Introduction must have been made").isTrue();
	}

	@Test
	public void testIntroductionDelegation() {
		((ICounter)noMethodsBean).increment();
		assertThat(counter.getCount()).as("Delegate's counter should be updated").isEqualTo(1);
	}

}


interface NoMethodsBean {
}


class NoMethodsBeanImpl implements NoMethodsBean {
}

