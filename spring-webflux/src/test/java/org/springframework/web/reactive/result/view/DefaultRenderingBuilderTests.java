package org.springframework.web.reactive.result.view;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DefaultRenderingBuilder}.
 *
 * @author Rossen Stoyanchev
 */
public class DefaultRenderingBuilderTests {

	@Test
	public void defaultValues() {
		Rendering rendering = Rendering.view("abc").build();

		assertThat(rendering.view()).isEqualTo("abc");
		assertThat(rendering.modelAttributes()).isEqualTo(Collections.emptyMap());
		assertThat(rendering.status()).isNull();
		assertThat(rendering.headers().size()).isEqualTo(0);
	}

	@Test
	public void defaultValuesForRedirect() throws Exception {
		Rendering rendering = Rendering.redirectTo("abc").build();

		Object view = rendering.view();
		assertThat(view.getClass()).isEqualTo(RedirectView.class);
		assertThat(((RedirectView) view).getUrl()).isEqualTo("abc");
		assertThat(((RedirectView) view).isContextRelative()).isTrue();
		assertThat(((RedirectView) view).isPropagateQuery()).isFalse();
	}


	@Test
	public void viewName() {
		Rendering rendering = Rendering.view("foo").build();
		assertThat(rendering.view()).isEqualTo("foo");
	}

	@Test
	public void modelAttribute() throws Exception {
		Foo foo = new Foo();
		Rendering rendering = Rendering.view("foo").modelAttribute(foo).build();

		assertThat(rendering.modelAttributes()).isEqualTo(Collections.singletonMap("foo", foo));
	}

	@Test
	public void modelAttributes() throws Exception {
		Foo foo = new Foo();
		Bar bar = new Bar();
		Rendering rendering = Rendering.view("foo").modelAttributes(foo, bar).build();

		Map<String, Object> map = new LinkedHashMap<>(2);
		map.put("foo", foo);
		map.put("bar", bar);
		assertThat(rendering.modelAttributes()).isEqualTo(map);
	}

	@Test
	public void model() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("foo", new Foo());
		map.put("bar", new Bar());
		Rendering rendering = Rendering.view("foo").model(map).build();

		assertThat(rendering.modelAttributes()).isEqualTo(map);
	}

	@Test
	public void header() throws Exception {
		Rendering rendering = Rendering.view("foo").header("foo", "bar").build();

		assertThat(rendering.headers().size()).isEqualTo(1);
		assertThat(rendering.headers().get("foo")).isEqualTo(Collections.singletonList("bar"));
	}

	@Test
	public void httpHeaders() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("foo", "bar");
		Rendering rendering = Rendering.view("foo").headers(headers).build();

		assertThat(rendering.headers()).isEqualTo(headers);
	}

	@Test
	public void redirectWithAbsoluteUrl() throws Exception {
		Rendering rendering = Rendering.redirectTo("foo").contextRelative(false).build();

		Object view = rendering.view();
		assertThat(view.getClass()).isEqualTo(RedirectView.class);
		assertThat(((RedirectView) view).isContextRelative()).isFalse();
	}

	@Test
	public void redirectWithPropagateQuery() throws Exception {
		Rendering rendering = Rendering.redirectTo("foo").propagateQuery(true).build();

		Object view = rendering.view();
		assertThat(view.getClass()).isEqualTo(RedirectView.class);
		assertThat(((RedirectView) view).isPropagateQuery()).isTrue();
	}


	private static class Foo {}

	private static class Bar {}

}
