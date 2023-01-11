package org.springframework.scheduling.quartz;

import org.junit.jupiter.api.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Fisher
 * @since 3.0
 */
public class QuartzSchedulerLifecycleTests {

	@Test  // SPR-6354
	public void destroyLazyInitSchedulerWithDefaultShutdownOrderDoesNotHang() {
		ConfigurableApplicationContext context =
				new ClassPathXmlApplicationContext("quartzSchedulerLifecycleTests.xml", getClass());
		assertThat(context.getBean("lazyInitSchedulerWithDefaultShutdownOrder")).isNotNull();
		StopWatch sw = new StopWatch();
		sw.start("lazyScheduler");
		context.close();
		sw.stop();
		assertThat(sw.getTotalTimeMillis() < 500).as("Quartz Scheduler with lazy-init is hanging on destruction: " +
				sw.getTotalTimeMillis()).isTrue();
	}

	@Test  // SPR-6354
	public void destroyLazyInitSchedulerWithCustomShutdownOrderDoesNotHang() {
		ConfigurableApplicationContext context =
				new ClassPathXmlApplicationContext("quartzSchedulerLifecycleTests.xml", getClass());
		assertThat(context.getBean("lazyInitSchedulerWithCustomShutdownOrder")).isNotNull();
		StopWatch sw = new StopWatch();
		sw.start("lazyScheduler");
		context.close();
		sw.stop();
		assertThat(sw.getTotalTimeMillis() < 500).as("Quartz Scheduler with lazy-init is hanging on destruction: " +
				sw.getTotalTimeMillis()).isTrue();
	}

}
