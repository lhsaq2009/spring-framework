package org.springframework.scheduling.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Fisher
 */
public class SchedulerBeanDefinitionParserTests {

	private ApplicationContext context;


	@BeforeEach
	public void setup() {
		this.context = new ClassPathXmlApplicationContext(
				"schedulerContext.xml", SchedulerBeanDefinitionParserTests.class);
	}

	@Test
	public void defaultScheduler() {
		ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) this.context.getBean("defaultScheduler");
		Integer size = (Integer) new DirectFieldAccessor(scheduler).getPropertyValue("poolSize");
		assertThat(size).isEqualTo(1);
	}

	@Test
	public void customScheduler() {
		ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) this.context.getBean("customScheduler");
		Integer size = (Integer) new DirectFieldAccessor(scheduler).getPropertyValue("poolSize");
		assertThat(size).isEqualTo(42);
	}

	@Test
	public void threadNamePrefix() {
		ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) this.context.getBean("customScheduler");
		assertThat(scheduler.getThreadNamePrefix()).isEqualTo("customScheduler-");
	}

}
