package org.springframework.web.reactive.result.view;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UrlBasedViewResolver}.
 *
 * @author Rossen Stoyanchev
 * @author Sebastien Deleuze
 * @author Sam Brannen
 */
public class UrlBasedViewResolverTests {

	private final UrlBasedViewResolver resolver = new UrlBasedViewResolver();


	@BeforeEach
	public void setup() {
		StaticApplicationContext context = new StaticApplicationContext();
		context.refresh();
		this.resolver.setApplicationContext(context);
	}

	@Test
	public void urlBasedViewResolverOverridesCustomRequestContextAttributeWithNonNullValue() throws Exception {
		assertThat(new TestView().getRequestContextAttribute())
			.as("requestContextAttribute when instantiated directly")
			.isEqualTo("testRequestContext");

		this.resolver.setViewClass(TestView.class);
		this.resolver.setRequestContextAttribute("viewResolverRequestContext");

		Mono<View> mono = this.resolver.resolveViewName("example", Locale.getDefault());
		StepVerifier.create(mono)
				.consumeNextWith(view -> {
					assertThat(view).isInstanceOf(TestView.class);
					assertThat(((TestView) view).getRequestContextAttribute())
						.as("requestContextAttribute when instantiated dynamically by UrlBasedViewResolver")
						.isEqualTo("viewResolverRequestContext");
				})
				.expectComplete()
				.verify(Duration.ZERO);
	}

	@Test
	public void urlBasedViewResolverDoesNotOverrideCustomRequestContextAttributeWithNull() throws Exception {
		assertThat(new TestView().getRequestContextAttribute())
			.as("requestContextAttribute when instantiated directly")
			.isEqualTo("testRequestContext");

		this.resolver.setViewClass(TestView.class);

		Mono<View> mono = this.resolver.resolveViewName("example", Locale.getDefault());
		StepVerifier.create(mono)
				.consumeNextWith(view -> {
					assertThat(view).isInstanceOf(TestView.class);
					assertThat(((TestView) view).getRequestContextAttribute())
						.as("requestContextAttribute when instantiated dynamically by UrlBasedViewResolver")
						.isEqualTo("testRequestContext");
				})
				.expectComplete()
				.verify(Duration.ZERO);
	}

	@Test
	public void viewNames() throws Exception {
		this.resolver.setViewClass(TestView.class);
		this.resolver.setViewNames("my*");

		Mono<View> mono = this.resolver.resolveViewName("my-view", Locale.US);
		assertThat(mono.block()).isNotNull();

		mono = this.resolver.resolveViewName("not-my-view", Locale.US);
		assertThat(mono.block()).isNull();
	}

	@Test
	public void redirectView() throws Exception {
		Mono<View> mono = this.resolver.resolveViewName("redirect:foo", Locale.US);

		StepVerifier.create(mono)
				.consumeNextWith(view -> {
					assertThat(view.getClass()).isEqualTo(RedirectView.class);
					RedirectView redirectView = (RedirectView) view;
					assertThat(redirectView.getUrl()).isEqualTo("foo");
					assertThat(redirectView.getStatusCode()).isEqualTo(HttpStatus.SEE_OTHER);
				})
				.expectComplete()
				.verify(Duration.ZERO);
	}

	@Test
	public void customizedRedirectView() throws Exception {
		this.resolver.setRedirectViewProvider(url -> new RedirectView(url, HttpStatus.FOUND));
		Mono<View> mono = this.resolver.resolveViewName("redirect:foo", Locale.US);

		StepVerifier.create(mono)
				.consumeNextWith(view -> {
					assertThat(view.getClass()).isEqualTo(RedirectView.class);
					RedirectView redirectView = (RedirectView) view;
					assertThat(redirectView.getUrl()).isEqualTo("foo");
					assertThat(redirectView.getStatusCode()).isEqualTo(HttpStatus.FOUND);
				})
				.expectComplete()
				.verify(Duration.ZERO);
	}


	private static class TestView extends AbstractUrlBasedView {

		public TestView() {
			setRequestContextAttribute("testRequestContext");
		}

		@Override
		public boolean checkResourceExists(Locale locale) throws Exception {
			return true;
		}

		@Override
		protected Mono<Void> renderInternal(Map<String, Object> attributes, MediaType contentType,
				ServerWebExchange exchange) {

			return Mono.empty();
		}
	}

}
