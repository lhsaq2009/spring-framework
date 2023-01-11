package org.springframework.web.reactive.handler;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import org.springframework.web.testfixture.server.handler.AbstractResponseStatusExceptionHandlerTests;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link WebFluxResponseStatusExceptionHandler}.
 *
 * @author Juergen Hoeller
 * @author Rossen Stoyanchev
 */
public class WebFluxResponseStatusExceptionHandlerTests extends AbstractResponseStatusExceptionHandlerTests {

	@Override
	protected ResponseStatusExceptionHandler createResponseStatusExceptionHandler() {
		return new WebFluxResponseStatusExceptionHandler();
	}


	@Test
	public void handleAnnotatedException() {
		Throwable ex = new CustomException();
		this.handler.handle(this.exchange, ex).block(Duration.ofSeconds(5));
		assertThat(this.exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.I_AM_A_TEAPOT);
	}

	@Test
	public void handleNestedAnnotatedException() {
		Throwable ex = new Exception(new CustomException());
		this.handler.handle(this.exchange, ex).block(Duration.ofSeconds(5));
		assertThat(this.exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.I_AM_A_TEAPOT);
	}


	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	private static class CustomException extends Exception {
	}

}
