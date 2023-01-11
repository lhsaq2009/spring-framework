package org.springframework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.jupiter.api.Test;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.testfixture.servlet.MockFilterConfig;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;
import org.springframework.web.testfixture.servlet.MockHttpServletResponse;
import org.springframework.web.testfixture.servlet.MockServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class RequestContextFilterTests {

	@Test
	public void happyPath() throws Exception {
		testFilterInvocation(null);
	}

	@Test
	public void withException() throws Exception {
		testFilterInvocation(new ServletException());
	}

	private void testFilterInvocation(final ServletException sex) throws Exception {
		final MockHttpServletRequest req = new MockHttpServletRequest();
		req.setAttribute("myAttr", "myValue");
		final MockHttpServletResponse resp = new MockHttpServletResponse();

		// Expect one invocation by the filter being tested
		class DummyFilterChain implements FilterChain {
			public int invocations = 0;
			@Override
			public void doFilter(ServletRequest req, ServletResponse resp) throws IOException, ServletException {
				++invocations;
				if (invocations == 1) {
					assertThat(RequestContextHolder.currentRequestAttributes().getAttribute("myAttr", RequestAttributes.SCOPE_REQUEST)).isSameAs("myValue");
					if (sex != null) {
						throw sex;
					}
				}
				else {
					throw new IllegalStateException("Too many invocations");
				}
			}
		}

		DummyFilterChain fc = new DummyFilterChain();
		MockFilterConfig mfc = new MockFilterConfig(new MockServletContext(), "foo");

		RequestContextFilter rbf = new RequestContextFilter();
		rbf.init(mfc);

		try {
			rbf.doFilter(req, resp, fc);
			assertThat(sex).isNull();
		}
		catch (ServletException ex) {
			assertThat(sex).isNotNull();
		}

		assertThatIllegalStateException().isThrownBy(
				RequestContextHolder::currentRequestAttributes);

		assertThat(fc.invocations).isEqualTo(1);
	}

}
