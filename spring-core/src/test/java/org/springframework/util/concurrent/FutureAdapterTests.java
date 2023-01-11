package org.springframework.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Arjen Poutsma
 */
class FutureAdapterTests {

	private FutureAdapter<String, Integer> adapter;

	private Future<Integer> adaptee;


	@BeforeEach
	@SuppressWarnings("unchecked")
	void setUp() {
		adaptee = mock(Future.class);
		adapter = new FutureAdapter<String, Integer>(adaptee) {
			@Override
			protected String adapt(Integer adapteeResult) throws ExecutionException {
				return adapteeResult.toString();
			}
		};
	}

	@Test
	void cancel() throws Exception {
		given(adaptee.cancel(true)).willReturn(true);
		boolean result = adapter.cancel(true);
		assertThat(result).isTrue();
	}

	@Test
	void isCancelled() {
		given(adaptee.isCancelled()).willReturn(true);
		boolean result = adapter.isCancelled();
		assertThat(result).isTrue();
	}

	@Test
	void isDone() {
		given(adaptee.isDone()).willReturn(true);
		boolean result = adapter.isDone();
		assertThat(result).isTrue();
	}

	@Test
	void get() throws Exception {
		given(adaptee.get()).willReturn(42);
		String result = adapter.get();
		assertThat(result).isEqualTo("42");
	}

	@Test
	void getTimeOut() throws Exception {
		given(adaptee.get(1, TimeUnit.SECONDS)).willReturn(42);
		String result = adapter.get(1, TimeUnit.SECONDS);
		assertThat(result).isEqualTo("42");
	}


}
