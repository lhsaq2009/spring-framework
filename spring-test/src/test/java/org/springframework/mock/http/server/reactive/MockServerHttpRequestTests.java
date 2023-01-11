package org.springframework.mock.http.server.reactive;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link MockServerHttpRequest}.
 * @author Rossen Stoyanchev
 */
class MockServerHttpRequestTests {

	@Test
	void cookieHeaderSet() throws Exception {
		HttpCookie foo11 = new HttpCookie("foo1", "bar1");
		HttpCookie foo12 = new HttpCookie("foo1", "bar2");
		HttpCookie foo21 = new HttpCookie("foo2", "baz1");
		HttpCookie foo22 = new HttpCookie("foo2", "baz2");

		MockServerHttpRequest request = MockServerHttpRequest.get("/")
				.cookie(foo11, foo12, foo21, foo22).build();

		assertThat(request.getCookies().get("foo1")).isEqualTo(Arrays.asList(foo11, foo12));
		assertThat(request.getCookies().get("foo2")).isEqualTo(Arrays.asList(foo21, foo22));
		assertThat(request.getHeaders().get(HttpHeaders.COOKIE)).isEqualTo(Arrays.asList("foo1=bar1", "foo1=bar2", "foo2=baz1", "foo2=baz2"));
	}

	@Test
	void queryParams() throws Exception {
		MockServerHttpRequest request = MockServerHttpRequest.get("/foo bar?a=b")
				.queryParam("name A", "value A1", "value A2")
				.queryParam("name B", "value B1")
				.build();

		assertThat(request.getURI().toString()).isEqualTo("/foo%20bar?a=b&name%20A=value%20A1&name%20A=value%20A2&name%20B=value%20B1");
	}

}
