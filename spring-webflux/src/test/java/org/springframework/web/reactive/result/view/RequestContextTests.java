package org.springframework.web.reactive.result.view;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.testfixture.http.server.reactive.MockServerHttpRequest;
import org.springframework.web.testfixture.server.MockServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link RequestContext}.
 * @author Rossen Stoyanchev
 */
public class RequestContextTests {

	private final MockServerWebExchange exchange =
			MockServerWebExchange.from(MockServerHttpRequest.get("/foo/path").contextPath("/foo"));

	private GenericApplicationContext applicationContext;

	private Map<String, Object> model = new HashMap<>();


	@BeforeEach
	public void init() {
		this.applicationContext = new GenericApplicationContext();
		this.applicationContext.refresh();
	}

	@Test
	public void testGetContextUrl() throws Exception {
		RequestContext context = new RequestContext(this.exchange, this.model, this.applicationContext);
		assertThat(context.getContextUrl("bar")).isEqualTo("/foo/bar");
	}

	@Test
	public void testGetContextUrlWithMap() throws Exception {
		RequestContext context = new RequestContext(this.exchange, this.model, this.applicationContext);
		Map<String, Object> map = new HashMap<>();
		map.put("foo", "bar");
		map.put("spam", "bucket");
		assertThat(context.getContextUrl("{foo}?spam={spam}", map)).isEqualTo("/foo/bar?spam=bucket");
	}

	@Test
	public void testGetContextUrlWithMapEscaping() throws Exception {
		RequestContext context = new RequestContext(this.exchange, this.model, this.applicationContext);
		Map<String, Object> map = new HashMap<>();
		map.put("foo", "bar baz");
		map.put("spam", "&bucket=");
		assertThat(context.getContextUrl("{foo}?spam={spam}", map)).isEqualTo("/foo/bar%20baz?spam=%26bucket%3D");
	}

}
