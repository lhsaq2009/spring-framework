package org.springframework.test.context.web;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Unit tests for {@link GenericXmlWebContextLoader}.
 *
 * @author Sam Brannen
 * @since 4.0.4
 */
class GenericXmlWebContextLoaderTests {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];


	@Test
	void configMustNotContainAnnotatedClasses() throws Exception {
		GenericXmlWebContextLoader loader = new GenericXmlWebContextLoader();
		WebMergedContextConfiguration mergedConfig = new WebMergedContextConfiguration(getClass(), EMPTY_STRING_ARRAY,
				new Class<?>[] { getClass() }, null, EMPTY_STRING_ARRAY, EMPTY_STRING_ARRAY, EMPTY_STRING_ARRAY,
				"resource/path", loader, null, null);
		assertThatIllegalStateException()
			.isThrownBy(() -> loader.loadContext(mergedConfig))
			.withMessageContaining("does not support annotated classes");
	}

}
