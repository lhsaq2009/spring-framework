package org.springframework.test.web.servlet.htmlunit;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Abstract base class for testing {@link WebRequestMatcher} implementations.
 *
 * @author Sam Brannen
 * @since 4.2
 */
abstract class AbstractWebRequestMatcherTests {

	protected void assertMatches(WebRequestMatcher matcher, String url) throws MalformedURLException {
		assertThat(matcher.matches(new WebRequest(new URL(url)))).isTrue();
	}

	protected void assertDoesNotMatch(WebRequestMatcher matcher, String url) throws MalformedURLException {
		assertThat(matcher.matches(new WebRequest(new URL(url)))).isFalse();
	}

}
