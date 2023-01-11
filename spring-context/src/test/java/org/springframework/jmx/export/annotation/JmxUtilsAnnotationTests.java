package org.springframework.jmx.export.annotation;

import javax.management.MXBean;

import org.junit.jupiter.api.Test;

import org.springframework.jmx.support.JmxUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 */
public class JmxUtilsAnnotationTests {

	@Test
	public void notMXBean() throws Exception {
		assertThat(JmxUtils.isMBean(FooNotX.class)).as("MXBean annotation not detected correctly").isFalse();
	}

	@Test
	public void annotatedMXBean() throws Exception {
		assertThat(JmxUtils.isMBean(FooX.class)).as("MXBean annotation not detected correctly").isTrue();
	}


	@MXBean(false)
	public interface FooNotMXBean {
		String getName();
	}

	public static class FooNotX implements FooNotMXBean {

		@Override
		public String getName() {
			return "Rob Harrop";
		}
	}

	@MXBean(true)
	public interface FooIfc {
		String getName();
	}

	public static class FooX implements FooIfc {

		@Override
		public String getName() {
			return "Rob Harrop";
		}
	}

}
