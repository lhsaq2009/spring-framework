package org.springframework.web.servlet.view.script;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;
import org.springframework.web.testfixture.servlet.MockHttpServletResponse;
import org.springframework.web.testfixture.servlet.MockServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for pure Javascript templates running on Nashorn engine.
 *
 * @author Sebastien Deleuze
 */
public class NashornScriptTemplateTests {

	private WebApplicationContext webAppContext;

	private ServletContext servletContext;


	@BeforeEach
	public void setup() {
		this.webAppContext = mock(WebApplicationContext.class);
		this.servletContext = new MockServletContext();
		this.servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webAppContext);
	}

	@Test
	public void renderTemplate() throws Exception {
		Map<String, Object> model = new HashMap<>();
		model.put("title", "Layout example");
		model.put("body", "This is the body");
		String url = "org/springframework/web/servlet/view/script/nashorn/template.html";
		MockHttpServletResponse response = render(url, model, ScriptTemplatingConfiguration.class);
		assertThat(response.getContentAsString()).isEqualTo("<html><head><title>Layout example</title></head><body><p>This is the body</p></body></html>");
	}

	@Test  // SPR-13453
	public void renderTemplateWithUrl() throws Exception {
		String url = "org/springframework/web/servlet/view/script/nashorn/template.html";
		MockHttpServletResponse response = render(url, null, ScriptTemplatingWithUrlConfiguration.class);
		assertThat(response.getContentAsString()).isEqualTo(("<html><head><title>Check url parameter</title></head><body><p>" + url + "</p></body></html>"));
	}

	private MockHttpServletResponse render(String viewUrl, Map<String, Object> model,
			Class<?> configuration) throws Exception {

		ScriptTemplateView view = createViewWithUrl(viewUrl, configuration);
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpServletRequest request = new MockHttpServletRequest();
		view.renderMergedOutputModel(model, request, response);
		return response;
	}

	private ScriptTemplateView createViewWithUrl(String viewUrl, Class<?> configuration) throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(configuration);
		ctx.refresh();

		ScriptTemplateView view = new ScriptTemplateView();
		view.setApplicationContext(ctx);
		view.setUrl(viewUrl);
		view.afterPropertiesSet();
		return view;
	}


	@Configuration
	static class ScriptTemplatingConfiguration {

		@Bean
		public ScriptTemplateConfigurer nashornConfigurer() {
			ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
			configurer.setEngineName("nashorn");
			configurer.setScripts("org/springframework/web/servlet/view/script/nashorn/render.js");
			configurer.setRenderFunction("render");
			return configurer;
		}
	}


	@Configuration
	static class ScriptTemplatingWithUrlConfiguration {

		@Bean
		public ScriptTemplateConfigurer nashornConfigurer() {
			ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
			configurer.setEngineName("nashorn");
			configurer.setScripts("org/springframework/web/servlet/view/script/nashorn/render.js");
			configurer.setRenderFunction("renderWithUrl");
			return configurer;
		}
	}

}
