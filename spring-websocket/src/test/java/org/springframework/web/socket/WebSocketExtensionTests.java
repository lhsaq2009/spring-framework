package org.springframework.web.socket;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test fixture for {@link WebSocketExtension}
 * @author Brian Clozel
 */
public class WebSocketExtensionTests {

	@Test
	public void parseHeaderSingle() {
		List<WebSocketExtension> extensions = WebSocketExtension.parseExtensions("x-test-extension ; foo=bar ; bar=baz");
		assertThat(extensions).hasSize(1);
		WebSocketExtension extension = extensions.get(0);

		assertThat(extension.getName()).isEqualTo("x-test-extension");
		assertThat(extension.getParameters().size()).isEqualTo(2);
		assertThat(extension.getParameters().get("foo")).isEqualTo("bar");
		assertThat(extension.getParameters().get("bar")).isEqualTo("baz");
	}

	@Test
	public void parseHeaderMultiple() {
		List<WebSocketExtension> extensions = WebSocketExtension.parseExtensions("x-foo-extension, x-bar-extension");
		assertThat(extensions.stream().map(WebSocketExtension::getName))
				.containsExactly("x-foo-extension", "x-bar-extension");
	}

}
