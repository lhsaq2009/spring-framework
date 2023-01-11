package org.springframework.test.web.servlet.samples.standalone;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Exception handling via {@code @ExceptionHandler} methods.
 *
 * @author Rossen Stoyanchev
 * @author Sam Brannen
 */
class ExceptionHandlerTests {

	@Nested
	class MvcTests {

		@Test
		void localExceptionHandlerMethod() throws Exception {
			standaloneSetup(new PersonController()).build()
				.perform(get("/person/Clyde"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("errorView"));
		}

		@Test
		void globalExceptionHandlerMethod() throws Exception {
			standaloneSetup(new PersonController()).setControllerAdvice(new GlobalExceptionHandler()).build()
				.perform(get("/person/Bonnie"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("globalErrorView"));
		}

		@Test
		void globalExceptionHandlerMethodUsingClassArgument() throws Exception {
			standaloneSetup(PersonController.class).setControllerAdvice(GlobalExceptionHandler.class).build()
				.perform(get("/person/Bonnie"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("globalErrorView"));
		}
	}


	@Controller
	private static class PersonController {

		@GetMapping("/person/{name}")
		String show(@PathVariable String name) {
			if (name.equals("Clyde")) {
				throw new IllegalArgumentException("simulated exception");
			}
			else if (name.equals("Bonnie")) {
				throw new IllegalStateException("simulated exception");
			}
			return "person/show";
		}

		@ExceptionHandler
		String handleException(IllegalArgumentException exception) {
			return "errorView";
		}
	}

	@ControllerAdvice
	private static class GlobalExceptionHandler {

		@ExceptionHandler
		String handleException(IllegalStateException exception) {
			return "globalErrorView";
		}
	}


	@Nested
	class RestTests {

		@Test
		void noException() throws Exception {
			standaloneSetup(RestPersonController.class)
				.setControllerAdvice(RestGlobalExceptionHandler.class, RestPersonControllerExceptionHandler.class).build()
				.perform(get("/person/Yoda").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Yoda"));
		}

		@Test
		void localExceptionHandlerMethod() throws Exception {
			standaloneSetup(RestPersonController.class)
				.setControllerAdvice(RestGlobalExceptionHandler.class, RestPersonControllerExceptionHandler.class).build()
				.perform(get("/person/Luke").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value("local - IllegalArgumentException"));
		}

		@Test
		void globalExceptionHandlerMethod() throws Exception {
			standaloneSetup(RestPersonController.class)
				.setControllerAdvice(RestGlobalExceptionHandler.class).build()
				.perform(get("/person/Leia").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value("global - IllegalStateException"));
		}

		@Test
		void globalRestPersonControllerExceptionHandlerTakesPrecedenceOverGlobalExceptionHandler() throws Exception {
			standaloneSetup(RestPersonController.class)
				.setControllerAdvice(RestGlobalExceptionHandler.class, RestPersonControllerExceptionHandler.class).build()
				.perform(get("/person/Leia").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value("globalPersonController - IllegalStateException"));
		}

		@Test  // gh-25520
		void noHandlerFound() throws Exception {
			standaloneSetup(RestPersonController.class)
				.setControllerAdvice(RestGlobalExceptionHandler.class, RestPersonControllerExceptionHandler.class)
				.addDispatcherServletCustomizer(dispatcherServlet -> dispatcherServlet.setThrowExceptionIfNoHandlerFound(true))
				.build()
				.perform(get("/bogus").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value("global - NoHandlerFoundException"));
		}
	}


	@RestController
	private static class RestPersonController {

		@GetMapping("/person/{name}")
		Person get(@PathVariable String name) {
			switch (name) {
				case "Luke":
					throw new IllegalArgumentException();
				case "Leia":
					throw new IllegalStateException();
				default:
					return new Person("Yoda");
			}
		}

		@ExceptionHandler
		Error handleException(IllegalArgumentException exception) {
			return new Error("local - " + exception.getClass().getSimpleName());
		}
	}

	@RestControllerAdvice(assignableTypes = RestPersonController.class)
	@Order(Ordered.HIGHEST_PRECEDENCE)
	private static class RestPersonControllerExceptionHandler {

		@ExceptionHandler
		Error handleException(Throwable exception) {
			return new Error("globalPersonController - " + exception.getClass().getSimpleName());
		}
	}

	@RestControllerAdvice
	@Order(Ordered.LOWEST_PRECEDENCE)
	private static class RestGlobalExceptionHandler {

		@ExceptionHandler
		Error handleException(Throwable exception) {
			return new Error( "global - " + exception.getClass().getSimpleName());
		}
	}

	static class Person {

		private final String name;

		Person(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	static class Error {

		private final String error;

		Error(String error) {
			this.error = error;
		}

		public String getError() {
			return error;
		}
	}

}
