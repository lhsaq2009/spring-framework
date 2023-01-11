package org.springframework.http.codec.json;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import org.springframework.core.ResolvableType;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

/**
 * Decode a byte stream into JSON and convert to Object's with Jackson 2.9,
 * leveraging non-blocking parsing.
 *
 * @author Sebastien Deleuze
 * @author Rossen Stoyanchev
 * @since 5.0
 * @see Jackson2JsonEncoder
 */
public class Jackson2JsonDecoder extends AbstractJackson2Decoder {

	private static final StringDecoder STRING_DECODER = StringDecoder.textPlainOnly(Arrays.asList(",", "\n"), false);

	private static final ResolvableType STRING_TYPE = ResolvableType.forClass(String.class);


	public Jackson2JsonDecoder() {
		super(Jackson2ObjectMapperBuilder.json().build());
	}

	public Jackson2JsonDecoder(ObjectMapper mapper, MimeType... mimeTypes) {
		super(mapper, mimeTypes);
	}

	@Override
	protected Flux<DataBuffer> processInput(Publisher<DataBuffer> input, ResolvableType elementType,
			@Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {

		Flux<DataBuffer> flux = Flux.from(input);
		if (mimeType == null) {
			return flux;
		}

		// Jackson asynchronous parser only supports UTF-8
		Charset charset = mimeType.getCharset();
		if (charset == null || StandardCharsets.UTF_8.equals(charset) || StandardCharsets.US_ASCII.equals(charset)) {
			return flux;
		}

		// Potentially, the memory consumption of this conversion could be improved by using CharBuffers instead
		// of allocating Strings, but that would require refactoring the buffer tokenization code from StringDecoder

		MimeType textMimeType = new MimeType(MimeTypeUtils.TEXT_PLAIN, charset);
		Flux<String> decoded = STRING_DECODER.decode(input, STRING_TYPE, textMimeType, null);
		DataBufferFactory factory = new DefaultDataBufferFactory();
		return decoded.map(s -> factory.wrap(s.getBytes(StandardCharsets.UTF_8)));
	}

}
