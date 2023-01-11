package org.springframework.mock.web;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Unit tests for {@link MockHttpSession}.
 *
 * @author Sam Brannen
 * @author Vedran Pavic
 * @since 3.2
 */
class MockHttpSessionTests {

	private MockHttpSession session = new MockHttpSession();


	@Test
	void invalidateOnce() {
		assertThat(session.isInvalid()).isFalse();
		session.invalidate();
		assertThat(session.isInvalid()).isTrue();
	}

	@Test
	void invalidateTwice() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(
				session::invalidate);
	}

	@Test
	void getCreationTimeOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(
				session::getCreationTime);
	}

	@Test
	void getLastAccessedTimeOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(
				session::getLastAccessedTime);
	}

	@Test
	void getAttributeOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(() ->
				session.getAttribute("foo"));
	}

	@Test
	void getAttributeNamesOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(
				session::getAttributeNames);
	}

	@Test
	void getValueOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(() ->
				session.getValue("foo"));
	}

	@Test
	void getValueNamesOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(
				session::getValueNames);
	}

	@Test
	void setAttributeOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(() ->
				session.setAttribute("name", "value"));
	}

	@Test
	void putValueOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(() ->
				session.putValue("name", "value"));
	}

	@Test
	void removeAttributeOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(() ->
				session.removeAttribute("name"));
	}

	@Test
	void removeValueOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(() ->
				session.removeValue("name"));
	}

	@Test
	void isNewOnInvalidatedSession() {
		session.invalidate();
		assertThatIllegalStateException().isThrownBy(
				session::isNew);
	}

	@Test
	void bindingListenerBindListener() {
		String bindingListenerName = "bindingListener";
		CountingHttpSessionBindingListener bindingListener = new CountingHttpSessionBindingListener();

		session.setAttribute(bindingListenerName, bindingListener);

		assertThat(1).isEqualTo(bindingListener.getCounter());
	}

	@Test
	void bindingListenerBindListenerThenUnbind() {
		String bindingListenerName = "bindingListener";
		CountingHttpSessionBindingListener bindingListener = new CountingHttpSessionBindingListener();

		session.setAttribute(bindingListenerName, bindingListener);
		session.removeAttribute(bindingListenerName);

		assertThat(0).isEqualTo(bindingListener.getCounter());
	}

	@Test
	void bindingListenerBindSameListenerTwice() {
		String bindingListenerName = "bindingListener";
		CountingHttpSessionBindingListener bindingListener = new CountingHttpSessionBindingListener();

		session.setAttribute(bindingListenerName, bindingListener);
		session.setAttribute(bindingListenerName, bindingListener);

		assertThat(1).isEqualTo(bindingListener.getCounter());
	}

	@Test
	void bindingListenerBindListenerOverwrite() {
		String bindingListenerName = "bindingListener";
		CountingHttpSessionBindingListener bindingListener1 = new CountingHttpSessionBindingListener();
		CountingHttpSessionBindingListener bindingListener2 = new CountingHttpSessionBindingListener();

		session.setAttribute(bindingListenerName, bindingListener1);
		session.setAttribute(bindingListenerName, bindingListener2);

		assertThat(0).isEqualTo(bindingListener1.getCounter());
		assertThat(1).isEqualTo(bindingListener2.getCounter());
	}

	private static class CountingHttpSessionBindingListener
			implements HttpSessionBindingListener {

		private final AtomicInteger counter = new AtomicInteger(0);

		@Override
		public void valueBound(HttpSessionBindingEvent event) {
			this.counter.incrementAndGet();
		}

		@Override
		public void valueUnbound(HttpSessionBindingEvent event) {
			this.counter.decrementAndGet();
		}

		int getCounter() {
			return this.counter.get();
		}

	}

}
