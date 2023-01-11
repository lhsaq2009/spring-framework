package org.springframework.http.codec.cbor;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.AbstractJackson2Decoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

/**
 * Decode bytes into CBOR and convert to Object's with Jackson.
 * Stream decoding is not supported yet.
 *
 * @author Sebastien Deleuze
 * @since 5.2
 * @see Jackson2CborEncoder
 * @see <a href="https://github.com/spring-projects/spring-framework/issues/20513">Add CBOR support to WebFlux</a>
 */
public class Jackson2CborDecoder extends AbstractJackson2Decoder {

	public Jackson2CborDecoder() {
		this(Jackson2ObjectMapperBuilder.cbor().build(), MediaType.APPLICATION_CBOR);
	}

	public Jackson2CborDecoder(ObjectMapper mapper, MimeType... mimeTypes) {
		super(mapper, mimeTypes);
		Assert.isAssignable(CBORFactory.class, mapper.getFactory().getClass());
	}


	@Override
	public Flux<Object> decode(Publisher<DataBuffer> input, ResolvableType elementType, MimeType mimeType, Map<String, Object> hints) {
		throw new UnsupportedOperationException("Does not support stream decoding yet");
	}

}
