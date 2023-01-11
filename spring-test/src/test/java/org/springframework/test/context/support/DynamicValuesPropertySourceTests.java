package org.springframework.test.context.support;

import java.util.HashMap;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DynamicValuesPropertySource}.
 *
 * @author Phillip Webb
 * @author Sam Brannen
 */
class DynamicValuesPropertySourceTests {

	@SuppressWarnings("serial")
	private final DynamicValuesPropertySource source = new DynamicValuesPropertySource("test",
		new HashMap<String, Supplier<Object>>() {{
			put("a", () -> "A");
			put("b", () -> "B");
		}});


	@Test
	void getPropertyReturnsSuppliedProperty() throws Exception {
		assertThat(this.source.getProperty("a")).isEqualTo("A");
		assertThat(this.source.getProperty("b")).isEqualTo("B");
	}

	@Test
	void getPropertyWhenMissingReturnsNull() throws Exception {
		assertThat(this.source.getProperty("c")).isNull();
	}

	@Test
	void containsPropertyWhenPresentReturnsTrue() {
		assertThat(this.source.containsProperty("a")).isTrue();
		assertThat(this.source.containsProperty("b")).isTrue();
	}

	@Test
	void containsPropertyWhenMissingReturnsFalse() {
		assertThat(this.source.containsProperty("c")).isFalse();
	}

	@Test
	void getPropertyNamesReturnsNames() {
		assertThat(this.source.getPropertyNames()).containsExactly("a", "b");
	}

}
