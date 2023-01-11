package org.springframework.web.servlet.config.annotation;

import javax.servlet.RequestDispatcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;
import org.springframework.web.testfixture.servlet.MockHttpServletResponse;
import org.springframework.web.testfixture.servlet.MockRequestDispatcher;
import org.springframework.web.testfixture.servlet.MockServletContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test fixture with a {@link DefaultServletHandlerConfigurer}.
 *
 * @author Rossen Stoyanchev
 */
public class DefaultServletHandlerConfigurerTests {

	private DefaultServletHandlerConfigurer configurer;

	private DispatchingMockServletContext servletContext;

	private MockHttpServletResponse response;


	@BeforeEach
	public void setup() {
		response = new MockHttpServletResponse();
		servletContext = new DispatchingMockServletContext();
		configurer = new DefaultServletHandlerConfigurer(servletContext);
	}


	@Test
	public void notEnabled() {
		assertThat(configurer.buildHandlerMapping()).isNull();
	}

	@Test
	public void enable() throws Exception {
		configurer.enable();
		SimpleUrlHandlerMapping handlerMapping = configurer.buildHandlerMapping();
		DefaultServletHttpRequestHandler handler = (DefaultServletHttpRequestHandler) handlerMapping.getUrlMap().get("/**");

		assertThat(handler).isNotNull();
		assertThat(handlerMapping.getOrder()).isEqualTo(Integer.MAX_VALUE);

		handler.handleRequest(new MockHttpServletRequest(), response);

		String expected = "default";
		assertThat(servletContext.url).as("The ServletContext was not called with the default servlet name").isEqualTo(expected);
		assertThat(response.getForwardedUrl()).as("The request was not forwarded").isEqualTo(expected);
	}

	@Test
	public void enableWithServletName() throws Exception {
		configurer.enable("defaultServlet");
		SimpleUrlHandlerMapping handlerMapping = configurer.buildHandlerMapping();
		DefaultServletHttpRequestHandler handler = (DefaultServletHttpRequestHandler) handlerMapping.getUrlMap().get("/**");

		assertThat(handler).isNotNull();
		assertThat(handlerMapping.getOrder()).isEqualTo(Integer.MAX_VALUE);

		handler.handleRequest(new MockHttpServletRequest(), response);

		String expected = "defaultServlet";
		assertThat(servletContext.url).as("The ServletContext was not called with the default servlet name").isEqualTo(expected);
		assertThat(response.getForwardedUrl()).as("The request was not forwarded").isEqualTo(expected);
	}


	private static class DispatchingMockServletContext extends MockServletContext {

		private String url;

		@Override
		public RequestDispatcher getNamedDispatcher(String url) {
			this.url = url;
			return new MockRequestDispatcher(url);
		}
	}

}
