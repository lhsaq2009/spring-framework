package org.springframework.web.reactive.result.method.annotation;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.AbstractHttpHandlerIntegrationTests;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.HttpServer;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

/**
 * @author Sebastien Deleuze
 * @author Sam Brannen
 */
class JacksonStreamingIntegrationTests extends AbstractHttpHandlerIntegrationTests {

	private AnnotationConfigApplicationContext wac;

	private WebClient webClient;


	@Override
	protected HttpHandler createHttpHandler() {
		this.wac = new AnnotationConfigApplicationContext();
		this.wac.register(TestConfiguration.class);
		this.wac.refresh();

		return WebHttpHandlerBuilder.webHandler(new DispatcherHandler(this.wac)).build();
	}

	@Override
	protected void startServer(HttpServer httpServer) throws Exception {
		super.startServer(httpServer);
		this.webClient = WebClient.create("http://localhost:" + this.port);
	}


	@ParameterizedHttpServerTest
	void jsonStreaming(HttpServer httpServer) throws Exception {
		startServer(httpServer);

		Flux<Person> result = this.webClient.get()
				.uri("/stream")
				.accept(APPLICATION_STREAM_JSON)
				.retrieve()
				.bodyToFlux(Person.class);

		StepVerifier.create(result)
				.expectNext(new Person("foo 0"))
				.expectNext(new Person("foo 1"))
				.thenCancel()
				.verify();
	}

	@ParameterizedHttpServerTest
	void smileStreaming(HttpServer httpServer) throws Exception {
		startServer(httpServer);

		Flux<Person> result = this.webClient.get()
				.uri("/stream")
				.accept(new MediaType("application", "stream+x-jackson-smile"))
				.retrieve()
				.bodyToFlux(Person.class);

		StepVerifier.create(result)
				.expectNext(new Person("foo 0"))
				.expectNext(new Person("foo 1"))
				.thenCancel()
				.verify();
	}


	@RestController
	@SuppressWarnings("unused")
	static class JacksonStreamingController {

		@GetMapping(value = "/stream",
				produces = { APPLICATION_STREAM_JSON_VALUE, "application/stream+x-jackson-smile" })
		Flux<Person> person() {
			return testInterval(Duration.ofMillis(100), 50).map(l -> new Person("foo " + l));
		}

	}

	@Configuration
	@EnableWebFlux
	@SuppressWarnings("unused")
	static class TestConfiguration {

		@Bean
		public JacksonStreamingController jsonStreamingController() {
			return new JacksonStreamingController();
		}
	}

	@SuppressWarnings("unused")
	private static class Person {

		private String name;

		public Person() {
		}

		public Person(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Person person = (Person) o;
			return !(this.name != null ? !this.name.equals(person.name) : person.name != null);
		}

		@Override
		public int hashCode() {
			return this.name != null ? this.name.hashCode() : 0;
		}

		@Override
		public String toString() {
			return "Person{" +
					"name='" + name + '\'' +
					'}';
		}
	}

}
