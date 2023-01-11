package org.springframework.beans;

import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Specific {@link DirectFieldAccessor} tests.
 *
 * @author Jose Luis Martin
 * @author Chris Beams
 * @author Stephane Nicoll
 */
public class DirectFieldAccessorTests extends AbstractPropertyAccessorTests {

	@Override
	protected DirectFieldAccessor createAccessor(Object target) {
		return new DirectFieldAccessor(target);
	}


	@Test
	public void withShadowedField() {
		final StringBuilder sb = new StringBuilder();

		TestBean target = new TestBean() {
			@SuppressWarnings("unused")
			StringBuilder name = sb;
		};

		DirectFieldAccessor dfa = createAccessor(target);
		assertThat(dfa.getPropertyType("name")).isEqualTo(StringBuilder.class);
		assertThat(dfa.getPropertyValue("name")).isEqualTo(sb);
	}

}
