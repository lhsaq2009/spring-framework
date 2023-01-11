package org.springframework.web.servlet.tags.form;

import java.io.Writer;

import javax.servlet.jsp.tagext.Tag;

import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rossen Stoyanchev
 */
public class ButtonTagTests extends AbstractFormTagTests {

	private ButtonTag tag;

	@Override
	protected void onSetUp() {
		this.tag = createTag(getWriter());
		this.tag.setParent(getFormTag());
		this.tag.setPageContext(getPageContext());
		this.tag.setId("My Id");
		this.tag.setName("My Name");
		this.tag.setValue("My Button");
	}

	@Test
	public void buttonTag() throws Exception {
		assertThat(this.tag.doStartTag()).isEqualTo(Tag.EVAL_BODY_INCLUDE);
		assertThat(this.tag.doEndTag()).isEqualTo(Tag.EVAL_PAGE);

		String output = getOutput();
		assertTagOpened(output);
		assertTagClosed(output);

		assertContainsAttribute(output, "id", "My Id");
		assertContainsAttribute(output, "name", "My Name");
		assertContainsAttribute(output, "type", "submit");
		assertContainsAttribute(output, "value", "My Button");
		assertAttributeNotPresent(output, "disabled");
	}

	@Test
	public void disabled() throws Exception {
		this.tag.setDisabled(true);

		this.tag.doStartTag();
		this.tag.doEndTag();

		String output = getOutput();
		assertTagOpened(output);
		assertTagClosed(output);

		assertContainsAttribute(output, "disabled", "disabled");
	}

	@Override
	protected TestBean createTestBean() {
		return new TestBean();
	}

	protected final void assertTagClosed(String output) {
		assertThat(output.endsWith("</button>")).as("Tag not closed properly").isTrue();
	}

	protected final void assertTagOpened(String output) {
		assertThat(output.startsWith("<button ")).as("Tag not opened properly").isTrue();
	}

	@SuppressWarnings("serial")
	protected ButtonTag createTag(final Writer writer) {
		return new ButtonTag() {
			@Override
			protected TagWriter createTagWriter() {
				return new TagWriter(writer);
			}
		};
	}

}
