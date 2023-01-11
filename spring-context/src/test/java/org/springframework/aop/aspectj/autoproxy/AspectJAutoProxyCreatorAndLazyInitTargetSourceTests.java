package org.springframework.aop.aspectj.autoproxy;

import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.beans.testfixture.beans.TestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Chris Beams
 */
public class AspectJAutoProxyCreatorAndLazyInitTargetSourceTests {

	@Test
	public void testAdrian() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());

		ITestBean adrian = (ITestBean) ctx.getBean("adrian");
		assertThat(LazyTestBean.instantiations).isEqualTo(0);
		assertThat(adrian).isNotNull();
		adrian.getAge();
		assertThat(adrian.getAge()).isEqualTo(68);
		assertThat(LazyTestBean.instantiations).isEqualTo(1);
	}

}


class LazyTestBean extends TestBean {

	public static int instantiations;

	public LazyTestBean() {
		++instantiations;
	}

}
