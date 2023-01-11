package org.springframework.context.annotation;

import example.scannable.FooService;
import example.scannable.ServiceInvocationCounter;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class SimpleScanTests {

	protected String[] getConfigLocations() {
		return new String[] {"simpleScanTests.xml"};
	}

	@Test
	public void testFooService() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(getConfigLocations(), getClass());

		FooService fooService = (FooService) ctx.getBean("fooServiceImpl");
		ServiceInvocationCounter serviceInvocationCounter = (ServiceInvocationCounter) ctx.getBean("serviceInvocationCounter");

		assertThat(serviceInvocationCounter.getCount()).isEqualTo(0);

		assertThat(fooService.isInitCalled()).isTrue();
		assertThat(serviceInvocationCounter.getCount()).isEqualTo(1);

		String value = fooService.foo(1);
		assertThat(value).isEqualTo("bar");
		assertThat(serviceInvocationCounter.getCount()).isEqualTo(2);

		fooService.foo(1);
		assertThat(serviceInvocationCounter.getCount()).isEqualTo(3);
	}

}
