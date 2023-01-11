package org.springframework.http.client.reactive;

import java.net.HttpCookie;
import java.net.URI;
import java.util.Collection;
import java.util.function.Function;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.reactive.client.ContentChunk;
import org.eclipse.jetty.reactive.client.ReactiveRequest;
import org.eclipse.jetty.reactive.client.internal.PublisherContentProvider;
import org.eclipse.jetty.util.Callback;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * {@link ClientHttpRequest} implementation for the Jetty ReactiveStreams HTTP client.
 *
 * @author Sebastien Deleuze
 * @since 5.1
 * @see <a href="https://github.com/jetty-project/jetty-reactive-httpclient">Jetty ReactiveStreams HttpClient</a>
 */
class JettyClientHttpRequest extends AbstractClientHttpRequest {

	private final Request jettyRequest;

	private final DataBufferFactory bufferFactory;


	public JettyClientHttpRequest(Request jettyRequest, DataBufferFactory bufferFactory) {
		this.jettyRequest = jettyRequest;
		this.bufferFactory = bufferFactory;
	}


	@Override
	public HttpMethod getMethod() {
		return HttpMethod.valueOf(this.jettyRequest.getMethod());
	}

	@Override
	public URI getURI() {
		return this.jettyRequest.getURI();
	}

	@Override
	public Mono<Void> setComplete() {
		return doCommit(this::completes);
	}

	@Override
	public DataBufferFactory bufferFactory() {
		return this.bufferFactory;
	}

	@Override
	public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
		ReactiveRequest.Content content = Flux.from(body)
				.map(this::toContentChunk)
				.as(chunks -> ReactiveRequest.Content.fromPublisher(chunks, getContentType()));
		this.jettyRequest.content(new PublisherContentProvider(content));
		return doCommit(this::completes);
	}

	@Override
	public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
		ReactiveRequest.Content content = Flux.from(body)
				.flatMap(Function.identity())
				.doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release)
				.map(this::toContentChunk)
				.as(chunks -> ReactiveRequest.Content.fromPublisher(chunks, getContentType()));
		this.jettyRequest.content(new PublisherContentProvider(content));
		return doCommit(this::completes);
	}

	private String getContentType() {
		MediaType contentType = getHeaders().getContentType();
		return contentType != null ? contentType.toString() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
	}

	private Mono<Void> completes() {
		return Mono.empty();
	}

	private ContentChunk toContentChunk(DataBuffer buffer) {
		return new ContentChunk(buffer.asByteBuffer(), new Callback() {
			@Override
			public void succeeded() {
				DataBufferUtils.release(buffer);
			}
			@Override
			public void failed(Throwable x) {
				DataBufferUtils.release(buffer);
				throw Exceptions.propagate(x);
			}
		});
	}


	@Override
	protected void applyCookies() {
		getCookies().values().stream().flatMap(Collection::stream)
				.map(cookie -> new HttpCookie(cookie.getName(), cookie.getValue()))
				.forEach(this.jettyRequest::cookie);
	}

	@Override
	protected void applyHeaders() {
		HttpHeaders headers = getHeaders();
		headers.forEach((key, value) -> value.forEach(v -> this.jettyRequest.header(key, v)));
		if (!headers.containsKey(HttpHeaders.ACCEPT)) {
			this.jettyRequest.header(HttpHeaders.ACCEPT, "*/*");
		}
	}

}
