package org.springframework.test.context.support;

import org.junit.jupiter.api.Test;

import org.springframework.test.context.MergedContextConfiguration;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Unit tests for {@link GenericXmlContextLoader}.
 *
 * @author Sam Brannen
 * @since 4.0.4
 * @see GenericXmlContextLoaderResourceLocationsTests
 */
class GenericXmlContextLoaderTests {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];


	@Test
	void configMustNotContainAnnotatedClasses() throws Exception {
		GenericXmlContextLoader loader = new GenericXmlContextLoader();
		MergedContextConfiguration mergedConfig = new MergedContextConfiguration(getClass(), EMPTY_STRING_ARRAY,
			new Class<?>[] { getClass() }, EMPTY_STRING_ARRAY, loader);
		assertThatIllegalStateException()
			.isThrownBy(() -> loader.loadContext(mergedConfig))
			.withMessageContaining("does not support annotated classes");
	}

}
