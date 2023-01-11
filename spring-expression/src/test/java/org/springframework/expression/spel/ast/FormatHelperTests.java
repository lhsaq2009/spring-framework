package org.springframework.expression.spel.ast;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.springframework.core.convert.TypeDescriptor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andy Wilkinson
 */
public class FormatHelperTests {

	@Test
	public void formatMethodWithSingleArgumentForMessage() {
		String message = FormatHelper.formatMethodForMessage("foo", Arrays.asList(TypeDescriptor.forObject("a string")));
		assertThat(message).isEqualTo("foo(java.lang.String)");
	}

	@Test
	public void formatMethodWithMultipleArgumentsForMessage() {
		String message = FormatHelper.formatMethodForMessage("foo", Arrays.asList(TypeDescriptor.forObject("a string"), TypeDescriptor.forObject(Integer.valueOf(5))));
		assertThat(message).isEqualTo("foo(java.lang.String,java.lang.Integer)");
	}

}
