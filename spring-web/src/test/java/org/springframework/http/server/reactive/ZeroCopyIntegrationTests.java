package org.springframework.http.server.reactive;

import java.io.File;
import java.net.URI;

import reactor.core.publisher.Mono;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.AbstractHttpHandlerIntegrationTests;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.HttpServer;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.ReactorHttpServer;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.UndertowHttpServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @author Arjen Poutsma
 */
class ZeroCopyIntegrationTests extends AbstractHttpHandlerIntegrationTests {

	private static final Resource springLogoResource = new ClassPathResource("/org/springframework/web/spring.png");

	private final ZeroCopyHandler handler = new ZeroCopyHandler();


	@Override
	protected HttpHandler createHttpHandler() {
		return this.handler;
	}


	@ParameterizedHttpServerTest
	void zeroCopy(HttpServer httpServer) throws Exception {
		assumeTrue(httpServer instanceof ReactorHttpServer || httpServer instanceof UndertowHttpServer,
			"Zero-copy does not support Servlet");

		startServer(httpServer);

		URI url = new URI("http://localhost:" + port);
		RequestEntity<?> request = RequestEntity.get(url).build();
		ResponseEntity<byte[]> response = new RestTemplate().exchange(request, byte[].class);

		assertThat(response.hasBody()).isTrue();
		assertThat(response.getHeaders().getContentLength()).isEqualTo(springLogoResource.contentLength());
		assertThat(response.getBody().length).isEqualTo(springLogoResource.contentLength());
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
	}


	private static class ZeroCopyHandler implements HttpHandler {

		@Override
		public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
			try {
				ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
				File logoFile = springLogoResource.getFile();
				zeroCopyResponse.getHeaders().setContentType(MediaType.IMAGE_PNG);
				zeroCopyResponse.getHeaders().setContentLength(logoFile.length());
				return zeroCopyResponse.writeWith(logoFile, 0, logoFile.length());
			}
			catch (Throwable ex) {
				return Mono.error(ex);
			}
		}
	}

}
