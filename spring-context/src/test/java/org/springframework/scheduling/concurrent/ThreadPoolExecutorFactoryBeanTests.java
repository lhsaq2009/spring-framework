package org.springframework.scheduling.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import org.junit.jupiter.api.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 */
public class ThreadPoolExecutorFactoryBeanTests {

	@Test
	public void defaultExecutor() throws Exception {
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(ExecutorConfig.class);
		ExecutorService executor = context.getBean(ExecutorService.class);

		FutureTask<String> task = new FutureTask<>(() -> "foo");
		executor.execute(task);
		assertThat(task.get()).isEqualTo("foo");
		context.close();
	}


	@Configuration
	public static class ExecutorConfig {

		@Bean
		public ThreadPoolExecutorFactoryBean executor() {
			return new ThreadPoolExecutorFactoryBean();
		}

	}

}
