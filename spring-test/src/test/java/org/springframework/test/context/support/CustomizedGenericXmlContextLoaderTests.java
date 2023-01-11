package org.springframework.test.context.support;

import org.junit.jupiter.api.Test;

import org.springframework.context.support.GenericApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test which verifies that extensions of
 * {@link AbstractGenericContextLoader} are able to <em>customize</em> the
 * newly created {@code ApplicationContext}. Specifically, this test
 * addresses the issues raised in <a
 * href="https://opensource.atlassian.com/projects/spring/browse/SPR-4008"
 * target="_blank">SPR-4008</a>: <em>Supply an opportunity to customize context
 * before calling refresh in ContextLoaders</em>.
 *
 * @author Sam Brannen
 * @since 2.5
 */
class CustomizedGenericXmlContextLoaderTests {

	@Test
	void customizeContext() throws Exception {
		StringBuilder builder = new StringBuilder();
		String expectedContents = "customizeContext() was called";

		new GenericXmlContextLoader() {

			@Override
			protected void customizeContext(GenericApplicationContext context) {
				assertThat(context.isActive()).as("The context should not yet have been refreshed.").isFalse();
				builder.append(expectedContents);
			}
		}.loadContext("classpath:/org/springframework/test/context/support/CustomizedGenericXmlContextLoaderTests-context.xml");

		assertThat(builder.toString()).as("customizeContext() should have been called.").isEqualTo(expectedContents);
	}

}
