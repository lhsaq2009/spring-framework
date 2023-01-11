package org.springframework.web.servlet.tags;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.junit.jupiter.api.Test;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.servlet.support.RequestContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 * @author Alef Arendsen
 */
public class ThemeTagTests extends AbstractTagTests {

	@Test
	@SuppressWarnings("serial")
	public void themeTag() throws JspException {
		PageContext pc = createPageContext();
		final StringBuffer message = new StringBuffer();
		ThemeTag tag = new ThemeTag() {
			@Override
			protected void writeMessage(String msg) {
				message.append(msg);
			}
		};
		tag.setPageContext(pc);
		tag.setCode("themetest");
		assertThat(tag.doStartTag() == Tag.EVAL_BODY_INCLUDE).as("Correct doStartTag return value").isTrue();
		assertThat(tag.doEndTag()).as("Correct doEndTag return value").isEqualTo(Tag.EVAL_PAGE);
		assertThat(message.toString()).isEqualTo("theme test message");
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void requestContext() throws ServletException {
		PageContext pc = createPageContext();
		RequestContext rc = new RequestContext((HttpServletRequest) pc.getRequest());
		assertThat(rc.getThemeMessage("themetest")).isEqualTo("theme test message");
		assertThat(rc.getThemeMessage("themetest", (String[]) null)).isEqualTo("theme test message");
		assertThat(rc.getThemeMessage("themetest", "default")).isEqualTo("theme test message");
		assertThat(rc.getThemeMessage("themetest", (Object[]) null, "default")).isEqualTo("theme test message");
		assertThat(rc.getThemeMessage("themetestArgs", new String[]{"arg1"})).isEqualTo("theme test message arg1");
		assertThat(rc.getThemeMessage("themetestArgs", Arrays.asList(new String[]{"arg1"}))).isEqualTo("theme test message arg1");
		assertThat(rc.getThemeMessage("themetesta", "default")).isEqualTo("default");
		assertThat(rc.getThemeMessage("themetesta", (List) null, "default")).isEqualTo("default");
		MessageSourceResolvable resolvable = new DefaultMessageSourceResolvable(new String[] {"themetest"});
		assertThat(rc.getThemeMessage(resolvable)).isEqualTo("theme test message");
	}

}
