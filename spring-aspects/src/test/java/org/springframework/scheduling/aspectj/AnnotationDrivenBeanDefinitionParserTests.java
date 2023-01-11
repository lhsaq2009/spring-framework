package org.springframework.scheduling.aspectj;

import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.config.TaskManagementConfigUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Nicoll
 */
public class AnnotationDrivenBeanDefinitionParserTests {

	private ConfigurableApplicationContext context;

	@BeforeEach
	public void setup() {
		this.context = new ClassPathXmlApplicationContext(
				"annotationDrivenContext.xml", AnnotationDrivenBeanDefinitionParserTests.class);
	}

	@AfterEach
	public void after() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void asyncAspectRegistered() {
		assertThat(context.containsBean(TaskManagementConfigUtils.ASYNC_EXECUTION_ASPECT_BEAN_NAME)).isTrue();
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void asyncPostProcessorExecutorReference() {
		Object executor = context.getBean("testExecutor");
		Object aspect = context.getBean(TaskManagementConfigUtils.ASYNC_EXECUTION_ASPECT_BEAN_NAME);
		assertThat(((Supplier) new DirectFieldAccessor(aspect).getPropertyValue("defaultExecutor")).get()).isSameAs(executor);
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void asyncPostProcessorExceptionHandlerReference() {
		Object exceptionHandler = context.getBean("testExceptionHandler");
		Object aspect = context.getBean(TaskManagementConfigUtils.ASYNC_EXECUTION_ASPECT_BEAN_NAME);
		assertThat(((Supplier) new DirectFieldAccessor(aspect).getPropertyValue("exceptionHandler")).get()).isSameAs(exceptionHandler);
	}

}
