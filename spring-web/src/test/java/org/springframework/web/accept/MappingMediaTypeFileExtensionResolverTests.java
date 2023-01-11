package org.springframework.web.accept;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test fixture for {@link MappingMediaTypeFileExtensionResolver}.
 *
 * @author Rossen Stoyanchev
 * @author Melissa Hartsock
 */
public class MappingMediaTypeFileExtensionResolverTests {

	private static final Map<String, MediaType> DEFAULT_MAPPINGS =
			Collections.singletonMap("json", MediaType.APPLICATION_JSON);


	@Test
	public void resolveExtensions() {
		List<String> extensions = new MappingMediaTypeFileExtensionResolver(DEFAULT_MAPPINGS)
				.resolveFileExtensions(MediaType.APPLICATION_JSON);

		assertThat(extensions).hasSize(1);
		assertThat(extensions.get(0)).isEqualTo("json");
	}

	@Test
	public void resolveExtensionsNoMatch() {
		assertThat(new MappingMediaTypeFileExtensionResolver(DEFAULT_MAPPINGS)
				.resolveFileExtensions(MediaType.TEXT_HTML)).isEmpty();
	}

	@Test // SPR-13747
	public void lookupMediaTypeCaseInsensitive() {
		assertThat(new MappingMediaTypeFileExtensionResolver(DEFAULT_MAPPINGS).lookupMediaType("JSON"))
				.isEqualTo(MediaType.APPLICATION_JSON);
	}

	@Test
	public void allFileExtensions() {
		Map<String, MediaType> mappings = new HashMap<>();
		mappings.put("json", MediaType.APPLICATION_JSON);
		mappings.put("JsOn", MediaType.APPLICATION_JSON);
		mappings.put("jSoN", MediaType.APPLICATION_JSON);

		MappingMediaTypeFileExtensionResolver resolver = new MappingMediaTypeFileExtensionResolver(mappings);
		assertThat(resolver.getAllFileExtensions()).containsExactly("json");
	}
}
