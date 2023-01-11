package org.springframework.test.context.hierarchies.web;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.2.2
 */
@ContextHierarchy(@ContextConfiguration)
public class DispatcherWacRootWacEarTests extends RootWacEarTests {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private String ear;

	@Autowired
	private String root;

	@Autowired
	private String dispatcher;


	@Disabled("Superseded by verifyDispatcherWacConfig()")
	@Test
	@Override
	void verifyEarConfig() {
		/* no-op */
	}

	@Disabled("Superseded by verifyDispatcherWacConfig()")
	@Test
	@Override
	void verifyRootWacConfig() {
		/* no-op */
	}

	@Test
	void verifyDispatcherWacConfig() {
		ApplicationContext parent = wac.getParent();
		assertThat(parent).isNotNull();
		boolean condition = parent instanceof WebApplicationContext;
		assertThat(condition).isTrue();

		ApplicationContext grandParent = parent.getParent();
		assertThat(grandParent).isNotNull();
		boolean condition1 = grandParent instanceof WebApplicationContext;
		assertThat(condition1).isFalse();

		ServletContext dispatcherServletContext = wac.getServletContext();
		assertThat(dispatcherServletContext).isNotNull();
		ServletContext rootServletContext = ((WebApplicationContext) parent).getServletContext();
		assertThat(rootServletContext).isNotNull();
		assertThat(rootServletContext).isSameAs(dispatcherServletContext);

		assertThat(rootServletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).isSameAs(parent);
		assertThat(dispatcherServletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).isSameAs(parent);

		assertThat(ear).isEqualTo("ear");
		assertThat(root).isEqualTo("root");
		assertThat(dispatcher).isEqualTo("dispatcher");
	}

}
