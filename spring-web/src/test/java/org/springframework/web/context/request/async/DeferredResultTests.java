package org.springframework.web.context.request.async;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import org.springframework.web.context.request.async.DeferredResult.DeferredResultHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * DeferredResult tests.
 *
 * @author Rossen Stoyanchev
 */
public class DeferredResultTests {

	@Test
	public void setResult() {
		DeferredResultHandler handler = mock(DeferredResultHandler.class);

		DeferredResult<String> result = new DeferredResult<>();
		result.setResultHandler(handler);

		assertThat(result.setResult("hello")).isTrue();
		verify(handler).handleResult("hello");
	}

	@Test
	public void setResultTwice() {
		DeferredResultHandler handler = mock(DeferredResultHandler.class);

		DeferredResult<String> result = new DeferredResult<>();
		result.setResultHandler(handler);

		assertThat(result.setResult("hello")).isTrue();
		assertThat(result.setResult("hi")).isFalse();

		verify(handler).handleResult("hello");
	}

	@Test
	public void isSetOrExpired() {
		DeferredResultHandler handler = mock(DeferredResultHandler.class);

		DeferredResult<String> result = new DeferredResult<>();
		result.setResultHandler(handler);

		assertThat(result.isSetOrExpired()).isFalse();

		result.setResult("hello");

		assertThat(result.isSetOrExpired()).isTrue();

		verify(handler).handleResult("hello");
	}

	@Test
	public void hasResult() {
		DeferredResultHandler handler = mock(DeferredResultHandler.class);

		DeferredResult<String> result = new DeferredResult<>();
		result.setResultHandler(handler);

		assertThat(result.hasResult()).isFalse();
		assertThat(result.getResult()).isNull();

		result.setResult("hello");

		assertThat(result.getResult()).isEqualTo("hello");
	}

	@Test
	public void onCompletion() throws Exception {
		final StringBuilder sb = new StringBuilder();

		DeferredResult<String> result = new DeferredResult<>();
		result.onCompletion(() -> sb.append("completion event"));

		result.getInterceptor().afterCompletion(null, null);

		assertThat(result.isSetOrExpired()).isTrue();
		assertThat(sb.toString()).isEqualTo("completion event");
	}

	@Test
	public void onTimeout() throws Exception {
		final StringBuilder sb = new StringBuilder();

		DeferredResultHandler handler = mock(DeferredResultHandler.class);

		DeferredResult<String> result = new DeferredResult<>(null, "timeout result");
		result.setResultHandler(handler);
		result.onTimeout(() -> sb.append("timeout event"));

		result.getInterceptor().handleTimeout(null, null);

		assertThat(sb.toString()).isEqualTo("timeout event");
		assertThat(result.setResult("hello")).as("Should not be able to set result a second time").isFalse();
		verify(handler).handleResult("timeout result");
	}

	@Test
	public void onError() throws Exception {
		final StringBuilder sb = new StringBuilder();

		DeferredResultHandler handler = mock(DeferredResultHandler.class);

		DeferredResult<String> result = new DeferredResult<>(null, "error result");
		result.setResultHandler(handler);
		Exception e = new Exception();
		result.onError(new Consumer<Throwable>() {
			@Override
			public void accept(Throwable t) {
				sb.append("error event");
			}
		});

		result.getInterceptor().handleError(null, null, e);

		assertThat(sb.toString()).isEqualTo("error event");
		assertThat(result.setResult("hello")).as("Should not be able to set result a second time").isFalse();
		verify(handler).handleResult(e);
	}

}
