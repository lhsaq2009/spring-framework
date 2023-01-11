package org.springframework.web.method.annotation;

import java.lang.reflect.Method;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;
import org.springframework.web.testfixture.servlet.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Test fixture with {@link org.springframework.web.method.annotation.AbstractCookieValueMethodArgumentResolver}.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 */
public class CookieValueMethodArgumentResolverTests {

	private AbstractCookieValueMethodArgumentResolver resolver;

	private MethodParameter paramNamedCookie;

	private MethodParameter paramNamedDefaultValueString;

	private MethodParameter paramString;

	private ServletWebRequest webRequest;

	private MockHttpServletRequest request;


	@BeforeEach
	public void setUp() throws Exception {
		resolver = new TestCookieValueMethodArgumentResolver();

		Method method = getClass().getMethod("params", Cookie.class, String.class, String.class);
		paramNamedCookie = new SynthesizingMethodParameter(method, 0);
		paramNamedDefaultValueString = new SynthesizingMethodParameter(method, 1);
		paramString = new SynthesizingMethodParameter(method, 2);

		request = new MockHttpServletRequest();
		webRequest = new ServletWebRequest(request, new MockHttpServletResponse());
	}


	@Test
	public void supportsParameter() {
		assertThat(resolver.supportsParameter(paramNamedCookie)).as("Cookie parameter not supported").isTrue();
		assertThat(resolver.supportsParameter(paramNamedDefaultValueString)).as("Cookie string parameter not supported").isTrue();
		assertThat(resolver.supportsParameter(paramString)).as("non-@CookieValue parameter supported").isFalse();
	}

	@Test
	public void resolveCookieDefaultValue() throws Exception {
		Object result = resolver.resolveArgument(paramNamedDefaultValueString, null, webRequest, null);

		boolean condition = result instanceof String;
		assertThat(condition).isTrue();
		assertThat(result).as("Invalid result").isEqualTo("bar");
	}

	@Test
	public void notFound() throws Exception {
		assertThatExceptionOfType(ServletRequestBindingException.class).isThrownBy(() ->
			resolver.resolveArgument(paramNamedCookie, null, webRequest, null));
	}

	private static class TestCookieValueMethodArgumentResolver extends AbstractCookieValueMethodArgumentResolver {

		public TestCookieValueMethodArgumentResolver() {
			super(null);
		}

		@Override
		protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
			return null;
		}
	}


	public void params(@CookieValue("name") Cookie param1,
			@CookieValue(name = "name", defaultValue = "bar") String param2,
			String param3) {
	}

}
