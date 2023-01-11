package org.springframework.context.annotation;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import example.scannable.FooService;
import example.scannable.ServiceInvocationCounter;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
public class SimpleConfigTests {

	@Test
	public void testFooService() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(getConfigLocations(), getClass());

		FooService fooService = ctx.getBean("fooServiceImpl", FooService.class);
		ServiceInvocationCounter serviceInvocationCounter = ctx.getBean("serviceInvocationCounter", ServiceInvocationCounter.class);

		String value = fooService.foo(1);
		assertThat(value).isEqualTo("bar");

		Future<?> future = fooService.asyncFoo(1);
		boolean condition = future instanceof FutureTask;
		assertThat(condition).isTrue();
		assertThat(future.get()).isEqualTo("bar");

		assertThat(serviceInvocationCounter.getCount()).isEqualTo(2);

		fooService.foo(1);
		assertThat(serviceInvocationCounter.getCount()).isEqualTo(3);
	}

	public String[] getConfigLocations() {
		return new String[] {"simpleConfigTests.xml"};
	}

}
