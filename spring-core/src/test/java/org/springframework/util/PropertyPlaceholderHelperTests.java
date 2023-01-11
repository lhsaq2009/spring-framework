package org.springframework.util;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Rob Harrop
 */
class PropertyPlaceholderHelperTests {

	private final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");

	@Test
	void withProperties() {
		String text = "foo=${foo}";
		Properties props = new Properties();
		props.setProperty("foo", "bar");

		assertThat(this.helper.replacePlaceholders(text, props)).isEqualTo("foo=bar");
	}

	@Test
	void withMultipleProperties() {
		String text = "foo=${foo},bar=${bar}";
		Properties props = new Properties();
		props.setProperty("foo", "bar");
		props.setProperty("bar", "baz");

		assertThat(this.helper.replacePlaceholders(text, props)).isEqualTo("foo=bar,bar=baz");
	}

	@Test
	void recurseInProperty() {
		String text = "foo=${bar}";
		Properties props = new Properties();
		props.setProperty("bar", "${baz}");
		props.setProperty("baz", "bar");

		assertThat(this.helper.replacePlaceholders(text, props)).isEqualTo("foo=bar");
	}

	@Test
	void recurseInPlaceholder() {
		String text = "foo=${b${inner}}";
		Properties props = new Properties();
		props.setProperty("bar", "bar");
		props.setProperty("inner", "ar");

		assertThat(this.helper.replacePlaceholders(text, props)).isEqualTo("foo=bar");

		text = "${top}";
		props = new Properties();
		props.setProperty("top", "${child}+${child}");
		props.setProperty("child", "${${differentiator}.grandchild}");
		props.setProperty("differentiator", "first");
		props.setProperty("first.grandchild", "actualValue");

		assertThat(this.helper.replacePlaceholders(text, props)).isEqualTo("actualValue+actualValue");
	}

	@Test
	void withResolver() {
		String text = "foo=${foo}";
		PlaceholderResolver resolver = placeholderName -> "foo".equals(placeholderName) ? "bar" : null;

		assertThat(this.helper.replacePlaceholders(text, resolver)).isEqualTo("foo=bar");
	}

	@Test
	void unresolvedPlaceholderIsIgnored() {
		String text = "foo=${foo},bar=${bar}";
		Properties props = new Properties();
		props.setProperty("foo", "bar");

		assertThat(this.helper.replacePlaceholders(text, props)).isEqualTo("foo=bar,bar=${bar}");
	}

	@Test
	void unresolvedPlaceholderAsError() {
		String text = "foo=${foo},bar=${bar}";
		Properties props = new Properties();
		props.setProperty("foo", "bar");

		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", null, false);
		assertThatIllegalArgumentException().isThrownBy(() ->
				helper.replacePlaceholders(text, props));
	}

}
