package org.springframework.aop.config;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.BeanFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Rick Evans
 * @author Chris Beams
 */
public class MethodLocatingFactoryBeanTests {

	private static final String BEAN_NAME = "string";
	private MethodLocatingFactoryBean factory;
	private BeanFactory beanFactory;

	@BeforeEach
	public void setUp() {
		factory = new MethodLocatingFactoryBean();
		beanFactory = mock(BeanFactory.class);
	}

	@Test
	public void testIsSingleton() {
		assertThat(factory.isSingleton()).isTrue();
	}

	@Test
	public void testGetObjectType() {
		assertThat(factory.getObjectType()).isEqualTo(Method.class);
	}

	@Test
	public void testWithNullTargetBeanName() {
		factory.setMethodName("toString()");
		assertThatIllegalArgumentException().isThrownBy(() ->
				factory.setBeanFactory(beanFactory));
	}

	@Test
	public void testWithEmptyTargetBeanName() {
		factory.setTargetBeanName("");
		factory.setMethodName("toString()");
		assertThatIllegalArgumentException().isThrownBy(() ->
				factory.setBeanFactory(beanFactory));
	}

	@Test
	public void testWithNullTargetMethodName() {
		factory.setTargetBeanName(BEAN_NAME);
		assertThatIllegalArgumentException().isThrownBy(() ->
				factory.setBeanFactory(beanFactory));
	}

	@Test
	public void testWithEmptyTargetMethodName() {
		factory.setTargetBeanName(BEAN_NAME);
		factory.setMethodName("");
		assertThatIllegalArgumentException().isThrownBy(() ->
				factory.setBeanFactory(beanFactory));
	}

	@Test
	public void testWhenTargetBeanClassCannotBeResolved() {
		factory.setTargetBeanName(BEAN_NAME);
		factory.setMethodName("toString()");
		assertThatIllegalArgumentException().isThrownBy(() ->
				factory.setBeanFactory(beanFactory));
		verify(beanFactory).getType(BEAN_NAME);
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testSunnyDayPath() throws Exception {
		given(beanFactory.getType(BEAN_NAME)).willReturn((Class)String.class);
		factory.setTargetBeanName(BEAN_NAME);
		factory.setMethodName("toString()");
		factory.setBeanFactory(beanFactory);
		Object result = factory.getObject();
		assertThat(result).isNotNull();
		boolean condition = result instanceof Method;
		assertThat(condition).isTrue();
		Method method = (Method) result;
		assertThat(method.invoke("Bingo")).isEqualTo("Bingo");
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testWhereMethodCannotBeResolved() {
		given(beanFactory.getType(BEAN_NAME)).willReturn((Class)String.class);
		factory.setTargetBeanName(BEAN_NAME);
		factory.setMethodName("loadOfOld()");
		assertThatIllegalArgumentException().isThrownBy(() ->
				factory.setBeanFactory(beanFactory));
	}

}
