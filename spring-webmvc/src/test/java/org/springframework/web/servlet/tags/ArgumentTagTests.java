package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.web.testfixture.servlet.MockBodyContent;
import org.springframework.web.testfixture.servlet.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ArgumentTag}
 *
 * @author Nicholas Williams
 */
public class ArgumentTagTests extends AbstractTagTests {

	private ArgumentTag tag;

	private MockArgumentSupportTag parent;

	@BeforeEach
	public void setUp() throws Exception {
		PageContext context = createPageContext();
		parent = new MockArgumentSupportTag();
		tag = new ArgumentTag();
		tag.setPageContext(context);
		tag.setParent(parent);
	}

	@Test
	public void argumentWithStringValue() throws JspException {
		tag.setValue("value1");

		int action = tag.doEndTag();

		assertThat(action).isEqualTo(Tag.EVAL_PAGE);
		assertThat(parent.getArgument()).isEqualTo("value1");
	}

	@Test
	public void argumentWithImplicitNullValue() throws JspException {
		int action = tag.doEndTag();

		assertThat(action).isEqualTo(Tag.EVAL_PAGE);
		assertThat(parent.getArgument()).isNull();
	}

	@Test
	public void argumentWithExplicitNullValue() throws JspException {
		tag.setValue(null);

		int action = tag.doEndTag();

		assertThat(action).isEqualTo(Tag.EVAL_PAGE);
		assertThat(parent.getArgument()).isNull();
	}

	@Test
	public void argumentWithBodyValue() throws JspException {
		tag.setBodyContent(new MockBodyContent("value2",
				new MockHttpServletResponse()));

		int action = tag.doEndTag();

		assertThat(action).isEqualTo(Tag.EVAL_PAGE);
		assertThat(parent.getArgument()).isEqualTo("value2");
	}

	@Test
	public void argumentWithValueThenReleaseThenBodyValue() throws JspException {
		tag.setValue("value3");

		int action = tag.doEndTag();

		assertThat(action).isEqualTo(Tag.EVAL_PAGE);
		assertThat(parent.getArgument()).isEqualTo("value3");

		tag.release();

		parent = new MockArgumentSupportTag();
		tag.setPageContext(createPageContext());
		tag.setParent(parent);
		tag.setBodyContent(new MockBodyContent("value4",
				new MockHttpServletResponse()));

		action = tag.doEndTag();

		assertThat(action).isEqualTo(Tag.EVAL_PAGE);
		assertThat(parent.getArgument()).isEqualTo("value4");
	}

	@SuppressWarnings("serial")
	private class MockArgumentSupportTag extends TagSupport implements ArgumentAware {

		Object argument;

		@Override
		public void addArgument(Object argument) {
			this.argument = argument;
		}

		private Object getArgument() {
			return argument;
		}
	}

}
