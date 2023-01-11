package org.springframework.test.web.reactive.server.samples.bind;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.ReactorHttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Sample tests demonstrating live server integration tests.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
public class HttpServerTests {

	private ReactorHttpServer server;

	private WebTestClient client;


	@BeforeEach
	public void start() throws Exception {
		HttpHandler httpHandler = RouterFunctions.toHttpHandler(
				route(GET("/test"), request -> ServerResponse.ok().bodyValue("It works!")));

		this.server = new ReactorHttpServer();
		this.server.setHandler(httpHandler);
		this.server.afterPropertiesSet();
		this.server.start();

		this.client = WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + this.server.getPort())
				.build();
	}

	@AfterEach
	public void stop() {
		this.server.stop();
	}


	@Test
	public void test() {
		this.client.get().uri("/test")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("It works!");
	}

}
