package org.springframework.web.cors.reactive;

import org.junit.jupiter.api.Test;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.testfixture.http.server.reactive.MockServerHttpRequest;
import org.springframework.web.testfixture.server.MockServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UrlBasedCorsConfigurationSource}.
 *
 * @author Sebastien Deleuze
 * @author Rossen Stoyanchev
 */
public class UrlBasedCorsConfigurationSourceTests {

	private final UrlBasedCorsConfigurationSource configSource
			= new UrlBasedCorsConfigurationSource();


	@Test
	public void empty() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/bar/test.html"));
		assertThat(this.configSource.getCorsConfiguration(exchange)).isNull();
	}

	@Test
	public void registerAndMatch() {
		CorsConfiguration config = new CorsConfiguration();
		this.configSource.registerCorsConfiguration("/bar/**", config);

		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/foo/test.html"));
		assertThat(this.configSource.getCorsConfiguration(exchange)).isNull();

		exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/bar/test.html"));
		assertThat(this.configSource.getCorsConfiguration(exchange)).isEqualTo(config);
	}

}
