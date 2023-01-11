package org.springframework.web.server.i18n;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.testfixture.http.server.reactive.MockServerHttpRequest;
import org.springframework.web.testfixture.server.MockServerWebExchange;

import static java.util.Locale.CANADA;
import static java.util.Locale.FRANCE;
import static java.util.Locale.US;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link FixedLocaleContextResolver}.
 *
 * @author Sebastien Deleuze
 */
public class FixedLocaleContextResolverTests {

	@BeforeEach
	public void setup() {
		Locale.setDefault(US);
	}

	@Test
	public void resolveDefaultLocale() {
		FixedLocaleContextResolver resolver = new FixedLocaleContextResolver();
		assertThat(resolver.resolveLocaleContext(exchange()).getLocale()).isEqualTo(US);
		assertThat(resolver.resolveLocaleContext(exchange(CANADA)).getLocale()).isEqualTo(US);
	}

	@Test
	public void resolveCustomizedLocale() {
		FixedLocaleContextResolver resolver = new FixedLocaleContextResolver(FRANCE);
		assertThat(resolver.resolveLocaleContext(exchange()).getLocale()).isEqualTo(FRANCE);
		assertThat(resolver.resolveLocaleContext(exchange(CANADA)).getLocale()).isEqualTo(FRANCE);
	}

	@Test
	public void resolveCustomizedAndTimeZoneLocale() {
		TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("UTC"));
		FixedLocaleContextResolver resolver = new FixedLocaleContextResolver(FRANCE, timeZone);
		TimeZoneAwareLocaleContext context = (TimeZoneAwareLocaleContext) resolver.resolveLocaleContext(exchange());
		assertThat(context.getLocale()).isEqualTo(FRANCE);
		assertThat(context.getTimeZone()).isEqualTo(timeZone);
	}

	private ServerWebExchange exchange(Locale... locales) {
		return MockServerWebExchange.from(MockServerHttpRequest.get("").acceptLanguageAsLocales(locales));
	}

}
