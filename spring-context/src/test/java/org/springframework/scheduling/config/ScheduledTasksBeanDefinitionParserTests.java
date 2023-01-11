package org.springframework.scheduling.config;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Fisher
 * @author Chris Beams
 */
@SuppressWarnings("unchecked")
public class ScheduledTasksBeanDefinitionParserTests {

	private ApplicationContext context;

	private ScheduledTaskRegistrar registrar;

	private Object testBean;


	@BeforeEach
	public void setup() {
		this.context = new ClassPathXmlApplicationContext(
				"scheduledTasksContext.xml", ScheduledTasksBeanDefinitionParserTests.class);
		this.registrar = this.context.getBeansOfType(
				ScheduledTaskRegistrar.class).values().iterator().next();
		this.testBean = this.context.getBean("testBean");
	}

	@Test
	public void checkScheduler() {
		Object schedulerBean = this.context.getBean("testScheduler");
		Object schedulerRef = new DirectFieldAccessor(this.registrar).getPropertyValue("taskScheduler");
		assertThat(schedulerRef).isEqualTo(schedulerBean);
	}

	@Test
	public void checkTarget() {
		List<IntervalTask> tasks = (List<IntervalTask>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("fixedRateTasks");
		Runnable runnable = tasks.get(0).getRunnable();
		assertThat(runnable.getClass()).isEqualTo(ScheduledMethodRunnable.class);
		Object targetObject = ((ScheduledMethodRunnable) runnable).getTarget();
		Method targetMethod = ((ScheduledMethodRunnable) runnable).getMethod();
		assertThat(targetObject).isEqualTo(this.testBean);
		assertThat(targetMethod.getName()).isEqualTo("test");
	}

	@Test
	public void fixedRateTasks() {
		List<IntervalTask> tasks = (List<IntervalTask>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("fixedRateTasks");
		assertThat(tasks.size()).isEqualTo(3);
		assertThat(tasks.get(0).getInterval()).isEqualTo(1000L);
		assertThat(tasks.get(1).getInterval()).isEqualTo(2000L);
		assertThat(tasks.get(2).getInterval()).isEqualTo(4000L);
		assertThat(tasks.get(2).getInitialDelay()).isEqualTo(500);
	}

	@Test
	public void fixedDelayTasks() {
		List<IntervalTask> tasks = (List<IntervalTask>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("fixedDelayTasks");
		assertThat(tasks.size()).isEqualTo(2);
		assertThat(tasks.get(0).getInterval()).isEqualTo(3000L);
		assertThat(tasks.get(1).getInterval()).isEqualTo(3500L);
		assertThat(tasks.get(1).getInitialDelay()).isEqualTo(250);
	}

	@Test
	public void cronTasks() {
		List<CronTask> tasks = (List<CronTask>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("cronTasks");
		assertThat(tasks.size()).isEqualTo(1);
		assertThat(tasks.get(0).getExpression()).isEqualTo("*/4 * 9-17 * * MON-FRI");
	}

	@Test
	public void triggerTasks() {
		List<TriggerTask> tasks = (List<TriggerTask>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("triggerTasks");
		assertThat(tasks.size()).isEqualTo(1);
		assertThat(tasks.get(0).getTrigger()).isInstanceOf(TestTrigger.class);
	}


	static class TestBean {

		public void test() {
		}
	}


	static class TestTrigger implements Trigger {

		@Override
		public Date nextExecutionTime(TriggerContext triggerContext) {
			return null;
		}
	}

}
