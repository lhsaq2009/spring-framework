package org.springframework.web.context.support;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;
import org.springframework.web.testfixture.servlet.MockHttpServletResponse;
import org.springframework.web.testfixture.servlet.MockServletConfig;
import org.springframework.web.testfixture.servlet.MockServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIOException;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 2.0
 */
public class HttpRequestHandlerTests {

	@Test
	public void testHttpRequestHandlerServletPassThrough() throws Exception {
		MockServletContext servletContext = new MockServletContext();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();

		StaticWebApplicationContext wac = new StaticWebApplicationContext();
		wac.getBeanFactory().registerSingleton("myHandler", new HttpRequestHandler() {
			@Override
			public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
				assertThat(req).isSameAs(request);
				assertThat(res).isSameAs(response);
				String exception = request.getParameter("exception");
				if ("ServletException".equals(exception)) {
					throw new ServletException("test");
				}
				if ("IOException".equals(exception)) {
					throw new IOException("test");
				}
				res.getWriter().write("myResponse");
			}
		});
		wac.setServletContext(servletContext);
		wac.refresh();
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);

		Servlet servlet = new HttpRequestHandlerServlet();
		servlet.init(new MockServletConfig(servletContext, "myHandler"));

		servlet.service(request, response);
		assertThat(response.getContentAsString()).isEqualTo("myResponse");

		request.setParameter("exception", "ServletException");
		assertThatExceptionOfType(ServletException.class).isThrownBy(() ->
				servlet.service(request, response))
			.withMessage("test");

		request.setParameter("exception", "IOException");
		assertThatIOException().isThrownBy(() ->
				servlet.service(request, response))
			.withMessage("test");
	}

}
