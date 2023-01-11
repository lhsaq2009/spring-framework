package org.springframework.context.annotation;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests ReflectionUtils methods as used against CGLIB-generated classes created
 * by ConfigurationClassEnhancer.
 *
 * @author Chris Beams
 * @since 3.1
 * @see org.springframework.util.ReflectionUtilsTests
 */
public class ReflectionUtilsIntegrationTests {

	@Test
	public void getUniqueDeclaredMethods_withCovariantReturnType_andCglibRewrittenMethodNames() throws Exception {
		Class<?> cglibLeaf = new ConfigurationClassEnhancer().enhance(Leaf.class, null);
		int m1MethodCount = 0;
		Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(cglibLeaf);
		for (Method method : methods) {
			if (method.getName().equals("m1")) {
				m1MethodCount++;
			}
		}
		assertThat(m1MethodCount).isEqualTo(1);
		for (Method method : methods) {
			if (method.getName().contains("m1")) {
				assertThat(Integer.class).isEqualTo(method.getReturnType());
			}
		}
	}


	@Configuration
	static abstract class Parent {
		public abstract Number m1();
	}


	@Configuration
	static class Leaf extends Parent {
		@Override
		@Bean
		public Integer m1() {
			return 42;
		}
	}

}
