package org.springframework.web.cors;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for {@link CorsUtils}.
 *
 * @author Sebastien Deleuze
 */
public class CorsUtilsTests {

	@Test
	public void isCorsRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.ORIGIN, "https://domain.com");
		assertThat(CorsUtils.isCorsRequest(request)).isTrue();
	}

	@Test
	public void isNotCorsRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		assertThat(CorsUtils.isCorsRequest(request)).isFalse();
	}

	@Test
	public void isPreFlightRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod(HttpMethod.OPTIONS.name());
		request.addHeader(HttpHeaders.ORIGIN, "https://domain.com");
		request.addHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET");
		assertThat(CorsUtils.isPreFlightRequest(request)).isTrue();
	}

	@Test
	public void isNotPreFlightRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		assertThat(CorsUtils.isPreFlightRequest(request)).isFalse();

		request = new MockHttpServletRequest();
		request.setMethod(HttpMethod.OPTIONS.name());
		request.addHeader(HttpHeaders.ORIGIN, "https://domain.com");
		assertThat(CorsUtils.isPreFlightRequest(request)).isFalse();
	}

}
