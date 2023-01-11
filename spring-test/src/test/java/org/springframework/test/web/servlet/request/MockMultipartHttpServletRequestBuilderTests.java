package org.springframework.test.web.servlet.request;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rossen Stoyanchev
 */
public class MockMultipartHttpServletRequestBuilderTests {

	@Test
	public void test() {
		MockHttpServletRequestBuilder parent = new MockHttpServletRequestBuilder(HttpMethod.GET, "/");
		parent.characterEncoding("UTF-8");
		Object result = new MockMultipartHttpServletRequestBuilder("/fileUpload").merge(parent);

		assertThat(result).isNotNull();
		assertThat(result.getClass()).isEqualTo(MockMultipartHttpServletRequestBuilder.class);

		MockMultipartHttpServletRequestBuilder builder = (MockMultipartHttpServletRequestBuilder) result;
		MockHttpServletRequest request = builder.buildRequest(new MockServletContext());
		assertThat(request.getCharacterEncoding()).isEqualTo("UTF-8");
	}

}
