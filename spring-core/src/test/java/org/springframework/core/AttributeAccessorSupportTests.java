package org.springframework.core;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 * @author Sam Brannen
 * @since 2.0
 */
class AttributeAccessorSupportTests {

	private static final String NAME = "foo";

	private static final String VALUE = "bar";

	private AttributeAccessor attributeAccessor = new SimpleAttributeAccessorSupport();

	@Test
	void setAndGet() throws Exception {
		this.attributeAccessor.setAttribute(NAME, VALUE);
		assertThat(this.attributeAccessor.getAttribute(NAME)).isEqualTo(VALUE);
	}

	@Test
	void setAndHas() throws Exception {
		assertThat(this.attributeAccessor.hasAttribute(NAME)).isFalse();
		this.attributeAccessor.setAttribute(NAME, VALUE);
		assertThat(this.attributeAccessor.hasAttribute(NAME)).isTrue();
	}

	@Test
	void remove() throws Exception {
		assertThat(this.attributeAccessor.hasAttribute(NAME)).isFalse();
		this.attributeAccessor.setAttribute(NAME, VALUE);
		assertThat(this.attributeAccessor.removeAttribute(NAME)).isEqualTo(VALUE);
		assertThat(this.attributeAccessor.hasAttribute(NAME)).isFalse();
	}

	@Test
	void attributeNames() throws Exception {
		this.attributeAccessor.setAttribute(NAME, VALUE);
		this.attributeAccessor.setAttribute("abc", "123");
		String[] attributeNames = this.attributeAccessor.attributeNames();
		Arrays.sort(attributeNames);
		assertThat(Arrays.binarySearch(attributeNames, NAME) > -1).isTrue();
		assertThat(Arrays.binarySearch(attributeNames, "abc") > -1).isTrue();
	}

	@SuppressWarnings("serial")
	private static class SimpleAttributeAccessorSupport extends AttributeAccessorSupport {
	}

}
