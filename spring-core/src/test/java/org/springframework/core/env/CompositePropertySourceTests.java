package org.springframework.core.env;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CompositePropertySource}.
 *
 * @author Phillip Webb
 */
class CompositePropertySourceTests {

	@Test
	void addFirst() {
		PropertySource<?> p1 = new MapPropertySource("p1", Collections.emptyMap());
		PropertySource<?> p2 = new MapPropertySource("p2", Collections.emptyMap());
		PropertySource<?> p3 = new MapPropertySource("p3", Collections.emptyMap());
		CompositePropertySource composite = new CompositePropertySource("c");
		composite.addPropertySource(p2);
		composite.addPropertySource(p3);
		composite.addPropertySource(p1);
		composite.addFirstPropertySource(p1);
		String s = composite.toString();
		int i1 = s.indexOf("name='p1'");
		int i2 = s.indexOf("name='p2'");
		int i3 = s.indexOf("name='p3'");
		assertThat(((i1 < i2) && (i2 < i3))).as("Bad order: " + s).isTrue();
	}

}
