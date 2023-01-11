package org.springframework.test.web.servlet;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Test fixture for {@link DefaultMvcResult}.
 *
 * @author Rossen Stoyanchev
 */
public class DefaultMvcResultTests {

	private final DefaultMvcResult mvcResult = new DefaultMvcResult(new MockHttpServletRequest(), null);

	@Test
	public void getAsyncResultSuccess() {
		this.mvcResult.setAsyncResult("Foo");
		this.mvcResult.setAsyncDispatchLatch(new CountDownLatch(0));
		this.mvcResult.getAsyncResult();
	}

	@Test
	public void getAsyncResultFailure() {
		assertThatIllegalStateException().isThrownBy(() ->
				this.mvcResult.getAsyncResult(0));
	}

}
