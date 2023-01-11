package org.springframework.web.servlet.i18n;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import org.springframework.web.testfixture.servlet.MockHttpServletRequest;
import org.springframework.web.testfixture.servlet.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 */
public class SessionLocaleResolverTests {

	@Test
	public void testResolveLocale() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.GERMAN);

		SessionLocaleResolver resolver = new SessionLocaleResolver();
		assertThat(resolver.resolveLocale(request)).isEqualTo(Locale.GERMAN);
	}

	@Test
	public void testSetAndResolveLocale() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setLocale(request, response, Locale.GERMAN);
		assertThat(resolver.resolveLocale(request)).isEqualTo(Locale.GERMAN);

		HttpSession session = request.getSession();
		request = new MockHttpServletRequest();
		request.setSession(session);
		resolver = new SessionLocaleResolver();

		assertThat(resolver.resolveLocale(request)).isEqualTo(Locale.GERMAN);
	}

	@Test
	public void testResolveLocaleWithoutSession() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addPreferredLocale(Locale.TAIWAN);

		SessionLocaleResolver resolver = new SessionLocaleResolver();

		assertThat(resolver.resolveLocale(request)).isEqualTo(request.getLocale());
	}

	@Test
	public void testResolveLocaleWithoutSessionAndDefaultLocale() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addPreferredLocale(Locale.TAIWAN);

		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.GERMAN);

		assertThat(resolver.resolveLocale(request)).isEqualTo(Locale.GERMAN);
	}

	@Test
	public void testSetLocaleToNullLocale() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addPreferredLocale(Locale.TAIWAN);
		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.GERMAN);
		MockHttpServletResponse response = new MockHttpServletResponse();

		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setLocale(request, response, null);
		Locale locale = (Locale) request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		assertThat(locale).isNull();

		HttpSession session = request.getSession();
		request = new MockHttpServletRequest();
		request.addPreferredLocale(Locale.TAIWAN);
		request.setSession(session);
		resolver = new SessionLocaleResolver();
		assertThat(resolver.resolveLocale(request)).isEqualTo(Locale.TAIWAN);
	}

}
