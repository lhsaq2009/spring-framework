package org.springframework.web.servlet.function;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Arjen Poutsma
 */
public class DefaultServerRequestBuilderTests {

	private final List<HttpMessageConverter<?>> messageConverters = Collections.singletonList(
			new StringHttpMessageConverter());

	@Test
	public void from() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "https://example.com");
		request.addHeader("foo", "bar");

		ServerRequest other = ServerRequest.create(request, messageConverters);

		ServerRequest result = ServerRequest.from(other)
				.method(HttpMethod.HEAD)
				.header("foo", "bar")
				.headers(httpHeaders -> httpHeaders.set("baz", "qux"))
				.cookie("foo", "bar")
				.cookies(cookies -> cookies.set("baz", new Cookie("baz", "qux")))
				.attribute("foo", "bar")
				.attributes(attributes -> attributes.put("baz", "qux"))
				.body("baz")
				.build();

		assertThat(result.method()).isEqualTo(HttpMethod.HEAD);
		assertThat(result.headers().asHttpHeaders().size()).isEqualTo(2);
		assertThat(result.headers().asHttpHeaders().getFirst("foo")).isEqualTo("bar");
		assertThat(result.headers().asHttpHeaders().getFirst("baz")).isEqualTo("qux");
		assertThat(result.cookies().size()).isEqualTo(2);
		assertThat(result.cookies().getFirst("foo").getValue()).isEqualTo("bar");
		assertThat(result.cookies().getFirst("baz").getValue()).isEqualTo("qux");
		assertThat(result.attributes().size()).isEqualTo(2);
		assertThat(result.attributes().get("foo")).isEqualTo("bar");
		assertThat(result.attributes().get("baz")).isEqualTo("qux");

		String body = result.body(String.class);
		assertThat(body).isEqualTo("baz");
	}

}
