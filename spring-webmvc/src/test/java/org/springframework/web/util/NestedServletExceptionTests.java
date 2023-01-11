package org.springframework.web.util;

import org.junit.jupiter.api.Test;

import org.springframework.core.NestedExceptionUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class NestedServletExceptionTests {

	@Test
	public void testNestedServletExceptionString() {
		NestedServletException exception = new NestedServletException("foo");
		assertThat(exception.getMessage()).isEqualTo("foo");
	}

	@Test
	public void testNestedServletExceptionStringThrowable() {
		Throwable cause = new RuntimeException();
		NestedServletException exception = new NestedServletException("foo", cause);
		assertThat(exception.getMessage()).isEqualTo(NestedExceptionUtils.buildMessage("foo", cause));
		assertThat(exception.getCause()).isEqualTo(cause);
	}

	@Test
	public void testNestedServletExceptionStringNullThrowable() {
		// This can happen if someone is sloppy with Throwable causes...
		NestedServletException exception = new NestedServletException("foo", null);
		assertThat(exception.getMessage()).isEqualTo("foo");
	}

}
