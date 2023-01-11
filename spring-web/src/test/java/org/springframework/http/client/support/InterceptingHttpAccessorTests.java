package org.springframework.http.client.support;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Tests for {@link InterceptingHttpAccessor}.
 *
 * @author Brian Clozel
 */
public class InterceptingHttpAccessorTests {

	@Test
	public void getInterceptors() {
		TestInterceptingHttpAccessor accessor = new TestInterceptingHttpAccessor();
		List<ClientHttpRequestInterceptor> interceptors = Arrays.asList(
				new SecondClientHttpRequestInterceptor(),
				new ThirdClientHttpRequestInterceptor(),
				new FirstClientHttpRequestInterceptor()

		);
		accessor.setInterceptors(interceptors);

		assertThat(accessor.getInterceptors().get(0)).isInstanceOf(FirstClientHttpRequestInterceptor.class);
		assertThat(accessor.getInterceptors().get(1)).isInstanceOf(SecondClientHttpRequestInterceptor.class);
		assertThat(accessor.getInterceptors().get(2)).isInstanceOf(ThirdClientHttpRequestInterceptor.class);
	}


	private class TestInterceptingHttpAccessor extends InterceptingHttpAccessor {
	}


	@Order(1)
	private class FirstClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
			return null;
		}
	}


	private class SecondClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
			return null;
		}

		@Override
		public int getOrder() {
			return 2;
		}
	}


	private class ThirdClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
			return null;
		}
	}

}
