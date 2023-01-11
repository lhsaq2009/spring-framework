package org.springframework.context.support;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Scott Andrews
 * @author Juergen Hoeller
 */
public class Spr7283Tests {

	@Test
	public void testListWithInconsistentElementType() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spr7283.xml", getClass());
		List<?> list = ctx.getBean("list", List.class);
		assertThat(list.size()).isEqualTo(2);
		boolean condition1 = list.get(0) instanceof A;
		assertThat(condition1).isTrue();
		boolean condition = list.get(1) instanceof B;
		assertThat(condition).isTrue();
	}


	public static class A {
		public A() {}
	}

	public static class B {
		public B() {}
	}

}
