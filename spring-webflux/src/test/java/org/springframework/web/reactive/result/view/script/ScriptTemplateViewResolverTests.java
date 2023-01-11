package org.springframework.web.reactive.result.view.script;

import org.junit.jupiter.api.Test;

import org.springframework.beans.DirectFieldAccessor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ScriptTemplateViewResolver}.
 *
 * @author Sebastien Deleuze
 */
public class ScriptTemplateViewResolverTests {

	@Test
	public void viewClass() throws Exception {
		ScriptTemplateViewResolver resolver = new ScriptTemplateViewResolver();
		assertThat(resolver.requiredViewClass()).isEqualTo(ScriptTemplateView.class);
		DirectFieldAccessor viewAccessor = new DirectFieldAccessor(resolver);
		Class<?> viewClass = (Class<?>) viewAccessor.getPropertyValue("viewClass");
		assertThat(viewClass).isEqualTo(ScriptTemplateView.class);
	}

}
