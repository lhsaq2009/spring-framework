package org.springframework.web.accept;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A test fixture with a test sub-class of AbstractMappingContentNegotiationStrategy.
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class MappingContentNegotiationStrategyTests {

	@Test
	public void resolveMediaTypes() throws Exception {
		Map<String, MediaType> mapping = Collections.singletonMap("json", MediaType.APPLICATION_JSON);
		TestMappingContentNegotiationStrategy strategy = new TestMappingContentNegotiationStrategy("json", mapping);

		List<MediaType> mediaTypes = strategy.resolveMediaTypes(null);

		assertThat(mediaTypes.size()).isEqualTo(1);
		assertThat(mediaTypes.get(0).toString()).isEqualTo("application/json");
	}

	@Test
	public void resolveMediaTypesNoMatch() throws Exception {
		Map<String, MediaType> mapping = null;
		TestMappingContentNegotiationStrategy strategy = new TestMappingContentNegotiationStrategy("blah", mapping);

		List<MediaType> mediaTypes = strategy.resolveMediaTypes(null);

		assertThat(mediaTypes).isEqualTo(ContentNegotiationStrategy.MEDIA_TYPE_ALL_LIST);
	}

	@Test
	public void resolveMediaTypesNoKey() throws Exception {
		Map<String, MediaType> mapping = Collections.singletonMap("json", MediaType.APPLICATION_JSON);
		TestMappingContentNegotiationStrategy strategy = new TestMappingContentNegotiationStrategy(null, mapping);

		List<MediaType> mediaTypes = strategy.resolveMediaTypes(null);

		assertThat(mediaTypes).isEqualTo(ContentNegotiationStrategy.MEDIA_TYPE_ALL_LIST);
	}

	@Test
	public void resolveMediaTypesHandleNoMatch() throws Exception {
		Map<String, MediaType> mapping = null;
		TestMappingContentNegotiationStrategy strategy = new TestMappingContentNegotiationStrategy("xml", mapping);

		List<MediaType> mediaTypes = strategy.resolveMediaTypes(null);

		assertThat(mediaTypes.size()).isEqualTo(1);
		assertThat(mediaTypes.get(0).toString()).isEqualTo("application/xml");
	}


	private static class TestMappingContentNegotiationStrategy extends AbstractMappingContentNegotiationStrategy {

		private final String extension;

		public TestMappingContentNegotiationStrategy(String extension, Map<String, MediaType> mapping) {
			super(mapping);
			this.extension = extension;
		}

		@Override
		protected String getMediaTypeKey(NativeWebRequest request) {
			return this.extension;
		}

		@Override
		protected MediaType handleNoMatch(NativeWebRequest request, String mappingKey) {
			return "xml".equals(mappingKey) ? MediaType.APPLICATION_XML : null;
		}
	}

}
