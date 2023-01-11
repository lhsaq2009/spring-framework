package org.springframework.web.servlet.tags.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.TestBean;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Rob Harrop
 */
public class HiddenInputTagTests extends AbstractFormTagTests {

	private HiddenInputTag tag;

	private TestBean bean;

	@Override
	@SuppressWarnings("serial")
	protected void onSetUp() {
		this.tag = new HiddenInputTag() {
			@Override
			protected TagWriter createTagWriter() {
				return new TagWriter(getWriter());
			}
		};
		this.tag.setPageContext(getPageContext());
	}

	@Test
	public void render() throws Exception {
		this.tag.setPath("name");
		int result = this.tag.doStartTag();
		assertThat(result).isEqualTo(Tag.SKIP_BODY);

		String output = getOutput();

		assertTagOpened(output);
		assertTagClosed(output);

		assertContainsAttribute(output, "type", "hidden");
		assertContainsAttribute(output, "value", "Sally Greenwood");
		assertAttributeNotPresent(output, "disabled");
	}

	@Test
	public void withCustomBinder() throws Exception {
		this.tag.setPath("myFloat");

		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(this.bean, COMMAND_NAME);
		errors.getPropertyAccessor().registerCustomEditor(Float.class, new SimpleFloatEditor());
		exposeBindingResult(errors);

		assertThat(this.tag.doStartTag()).isEqualTo(Tag.SKIP_BODY);

		String output = getOutput();

		assertTagOpened(output);
		assertTagClosed(output);

		assertContainsAttribute(output, "type", "hidden");
		assertContainsAttribute(output, "value", "12.34f");
	}

	@Test
	public void dynamicTypeAttribute() throws JspException {
		assertThatIllegalArgumentException().isThrownBy(() ->
				this.tag.setDynamicAttribute(null, "type", "email"))
			.withMessage("Attribute type=\"email\" is not allowed");
	}

	@Test
	public void disabledTrue() throws Exception {
		this.tag.setDisabled(true);

		this.tag.doStartTag();
		this.tag.doEndTag();

		String output = getOutput();
		assertTagOpened(output);
		assertTagClosed(output);

		assertContainsAttribute(output, "disabled", "disabled");
	}

	// SPR-8661

	@Test
	public void disabledFalse() throws Exception {
		this.tag.setDisabled(false);

		this.tag.doStartTag();
		this.tag.doEndTag();

		String output = getOutput();
		assertTagOpened(output);
		assertTagClosed(output);

		assertAttributeNotPresent(output, "disabled");
	}

	private void assertTagClosed(String output) {
		assertThat(output.endsWith("/>")).isTrue();
	}

	private void assertTagOpened(String output) {
		assertThat(output.startsWith("<input ")).isTrue();
	}

	@Override
	protected TestBean createTestBean() {
		this.bean = new TestBean();
		bean.setName("Sally Greenwood");
		bean.setMyFloat(new Float("12.34"));
		return bean;
	}

}
