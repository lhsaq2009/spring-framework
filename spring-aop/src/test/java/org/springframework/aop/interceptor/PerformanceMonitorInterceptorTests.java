package org.springframework.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Rob Harrop
 * @author Rick Evans
 * @author Chris Beams
 */
public class PerformanceMonitorInterceptorTests {

	@Test
	public void testSuffixAndPrefixAssignment() {
		PerformanceMonitorInterceptor interceptor = new PerformanceMonitorInterceptor();

		assertThat(interceptor.getPrefix()).isNotNull();
		assertThat(interceptor.getSuffix()).isNotNull();

		interceptor.setPrefix(null);
		interceptor.setSuffix(null);

		assertThat(interceptor.getPrefix()).isNotNull();
		assertThat(interceptor.getSuffix()).isNotNull();
	}

	@Test
	public void testSunnyDayPathLogsPerformanceMetricsCorrectly() throws Throwable {
		MethodInvocation mi = mock(MethodInvocation.class);
		given(mi.getMethod()).willReturn(String.class.getMethod("toString", new Class[0]));

		Log log = mock(Log.class);

		PerformanceMonitorInterceptor interceptor = new PerformanceMonitorInterceptor(true);
		interceptor.invokeUnderTrace(mi, log);

		verify(log).trace(anyString());
	}

	@Test
	public void testExceptionPathStillLogsPerformanceMetricsCorrectly() throws Throwable {
		MethodInvocation mi = mock(MethodInvocation.class);

		given(mi.getMethod()).willReturn(String.class.getMethod("toString", new Class[0]));
		given(mi.proceed()).willThrow(new IllegalArgumentException());
		Log log = mock(Log.class);

		PerformanceMonitorInterceptor interceptor = new PerformanceMonitorInterceptor(true);
		assertThatIllegalArgumentException().isThrownBy(() ->
				interceptor.invokeUnderTrace(mi, log));

		verify(log).trace(anyString());
	}

}
