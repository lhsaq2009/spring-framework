package org.springframework.web.cors;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpMethod;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Unit tests for {@link UrlBasedCorsConfigurationSource}.
 * @author Sebastien Deleuze
 */
public class UrlBasedCorsConfigurationSourceTests {

	private final UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();

	@Test
	public void empty() {
		MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "/bar/test.html");
		assertThat(this.configSource.getCorsConfiguration(request)).isNull();
	}

	@Test
	public void registerAndMatch() {
		CorsConfiguration config = new CorsConfiguration();
		this.configSource.registerCorsConfiguration("/bar/**", config);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo/test.html");
		assertThat(this.configSource.getCorsConfiguration(request)).isNull();

		request.setRequestURI("/bar/test.html");
		assertThat(this.configSource.getCorsConfiguration(request)).isEqualTo(config);
	}

	@Test
	public void unmodifiableConfigurationsMap() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() ->
				this.configSource.getCorsConfigurations().put("/**", new CorsConfiguration()));
	}

}
