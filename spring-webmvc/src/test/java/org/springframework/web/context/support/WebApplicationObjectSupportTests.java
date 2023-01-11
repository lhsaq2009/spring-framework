package org.springframework.web.context.support;

import java.io.File;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.web.testfixture.servlet.MockServletContext;
import org.springframework.web.util.WebUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 28.08.2003
 */
public class WebApplicationObjectSupportTests {

	@Test
	@SuppressWarnings("resource")
	public void testWebApplicationObjectSupport() {
		StaticWebApplicationContext wac = new StaticWebApplicationContext();
		wac.setServletContext(new MockServletContext());
		File tempDir = new File("");
		wac.getServletContext().setAttribute(WebUtils.TEMP_DIR_CONTEXT_ATTRIBUTE, tempDir);
		wac.registerBeanDefinition("test", new RootBeanDefinition(TestWebApplicationObject.class));
		wac.refresh();
		WebApplicationObjectSupport wao = (WebApplicationObjectSupport) wac.getBean("test");
		assertThat(wac.getServletContext()).isEqualTo(wao.getServletContext());
		assertThat(tempDir).isEqualTo(wao.getTempDir());
	}

	@Test
	@SuppressWarnings("resource")
	public void testWebApplicationObjectSupportWithWrongContext() {
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerBeanDefinition("test", new RootBeanDefinition(TestWebApplicationObject.class));
		WebApplicationObjectSupport wao = (WebApplicationObjectSupport) ac.getBean("test");
		assertThatIllegalStateException().isThrownBy(
				wao::getWebApplicationContext);
	}


	public static class TestWebApplicationObject extends WebApplicationObjectSupport {
	}

}
