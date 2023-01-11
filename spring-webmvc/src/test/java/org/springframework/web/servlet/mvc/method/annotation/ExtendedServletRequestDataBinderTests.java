package org.springframework.web.servlet.mvc.method.annotation;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.TestBean;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test fixture for {@link ExtendedServletRequestDataBinder}.
 *
 * @author Rossen Stoyanchev
 */
public class ExtendedServletRequestDataBinderTests {

	private MockHttpServletRequest request;

	@BeforeEach
	public void setup() {
		this.request = new MockHttpServletRequest();
	}

	@Test
	public void createBinder() throws Exception {
		Map<String, String> uriTemplateVars = new HashMap<>();
		uriTemplateVars.put("name", "nameValue");
		uriTemplateVars.put("age", "25");
		request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVars);

		TestBean target = new TestBean();
		WebDataBinder binder = new ExtendedServletRequestDataBinder(target, "");
		((ServletRequestDataBinder) binder).bind(request);

		assertThat(target.getName()).isEqualTo("nameValue");
		assertThat(target.getAge()).isEqualTo(25);
	}

	@Test
	public void uriTemplateVarAndRequestParam() throws Exception {
		request.addParameter("age", "35");

		Map<String, String> uriTemplateVars = new HashMap<>();
		uriTemplateVars.put("name", "nameValue");
		uriTemplateVars.put("age", "25");
		request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVars);

		TestBean target = new TestBean();
		WebDataBinder binder = new ExtendedServletRequestDataBinder(target, "");
		((ServletRequestDataBinder) binder).bind(request);

		assertThat(target.getName()).isEqualTo("nameValue");
		assertThat(target.getAge()).isEqualTo(35);
	}

	@Test
	public void noUriTemplateVars() throws Exception {
		TestBean target = new TestBean();
		WebDataBinder binder = new ExtendedServletRequestDataBinder(target, "");
		((ServletRequestDataBinder) binder).bind(request);

		assertThat(target.getName()).isEqualTo(null);
		assertThat(target.getAge()).isEqualTo(0);
	}

}
