package org.springframework.http.codec.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.HttpMessageWriter;

/**
 * Default implementation of {@link ClientCodecConfigurer}.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
public class DefaultClientCodecConfigurer extends BaseCodecConfigurer implements ClientCodecConfigurer {


	public DefaultClientCodecConfigurer() {
		super(new ClientDefaultCodecsImpl());
		((ClientDefaultCodecsImpl) defaultCodecs()).setPartWritersSupplier(this::getPartWriters);
	}

	private DefaultClientCodecConfigurer(DefaultClientCodecConfigurer other) {
		super(other);
		((ClientDefaultCodecsImpl) defaultCodecs()).setPartWritersSupplier(this::getPartWriters);
	}


	@Override
	public ClientDefaultCodecs defaultCodecs() {
		return (ClientDefaultCodecs) super.defaultCodecs();
	}

	@Override
	public DefaultClientCodecConfigurer clone() {
		return new DefaultClientCodecConfigurer(this);
	}

	@Override
	protected BaseDefaultCodecs cloneDefaultCodecs() {
		return new ClientDefaultCodecsImpl((ClientDefaultCodecsImpl) defaultCodecs());
	}

	private List<HttpMessageWriter<?>> getPartWriters() {
		List<HttpMessageWriter<?>> result = new ArrayList<>();
		result.addAll(this.customCodecs.getTypedWriters().keySet());
		result.addAll(this.defaultCodecs.getBaseTypedWriters());
		result.addAll(this.customCodecs.getObjectWriters().keySet());
		result.addAll(this.defaultCodecs.getBaseObjectWriters());
		result.addAll(this.defaultCodecs.getCatchAllWriters());
		return result;
	}

}
