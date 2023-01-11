package org.springframework.messaging.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.messaging.MessageHeaders;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeTypeUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Test fixture for {@link org.springframework.messaging.converter.DefaultContentTypeResolver}.
 *
 * @author Rossen Stoyanchev
 */
public class DefaultContentTypeResolverTests {

	private DefaultContentTypeResolver resolver;


	@BeforeEach
	public void setup() {
		this.resolver = new DefaultContentTypeResolver();
	}

	@Test
	public void resolve() {
		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		MessageHeaders headers = new MessageHeaders(map);

		assertThat(this.resolver.resolve(headers)).isEqualTo(MimeTypeUtils.APPLICATION_JSON);
	}

	@Test
	public void resolveStringContentType() {
		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);
		MessageHeaders headers = new MessageHeaders(map);

		assertThat(this.resolver.resolve(headers)).isEqualTo(MimeTypeUtils.APPLICATION_JSON);
	}

	@Test
	public void resolveInvalidStringContentType() {
		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, "invalidContentType");
		MessageHeaders headers = new MessageHeaders(map);
		assertThatExceptionOfType(InvalidMimeTypeException.class).isThrownBy(() ->
				this.resolver.resolve(headers));
	}

	@Test
	public void resolveUnknownHeaderType() {
		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, 1);
		MessageHeaders headers = new MessageHeaders(map);
		assertThatIllegalArgumentException().isThrownBy(() ->
				this.resolver.resolve(headers));
	}

	@Test
	public void resolveNoContentTypeHeader() {
		MessageHeaders headers = new MessageHeaders(Collections.<String, Object>emptyMap());

		assertThat(this.resolver.resolve(headers)).isNull();
	}

	@Test
	public void resolveDefaultMimeType() {
		this.resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		MessageHeaders headers = new MessageHeaders(Collections.<String, Object>emptyMap());

		assertThat(this.resolver.resolve(headers)).isEqualTo(MimeTypeUtils.APPLICATION_JSON);
	}

}
