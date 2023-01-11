package org.springframework.http;

import org.junit.jupiter.api.Test;

import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Arjen Poutsma
 */
public class MediaTypeFactoryTests {

	@Test
	public void getMediaType() {
		assertThat(MediaTypeFactory.getMediaType("file.xml").get()).isEqualTo(MediaType.APPLICATION_XML);
		assertThat(MediaTypeFactory.getMediaType("file.js").get()).isEqualTo(MediaType.parseMediaType("application/javascript"));
		assertThat(MediaTypeFactory.getMediaType("file.css").get()).isEqualTo(MediaType.parseMediaType("text/css"));
		assertThat(MediaTypeFactory.getMediaType("file.foobar").isPresent()).isFalse();
	}

	@Test
	public void nullParameter() {
		assertThat(MediaTypeFactory.getMediaType((String) null).isPresent()).isFalse();
		assertThat(MediaTypeFactory.getMediaType((Resource) null).isPresent()).isFalse();
		assertThat(MediaTypeFactory.getMediaTypes(null).isEmpty()).isTrue();
	}

}
