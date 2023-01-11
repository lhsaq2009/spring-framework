package org.springframework.validation;

import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.TestBean;
import org.springframework.validation.DefaultMessageCodesResolver.Format;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultMessageCodesResolver}.
 *
 * @author Phillip Webb
 */
public class DefaultMessageCodesResolverTests {

	private DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();

	@Test
	public void shouldResolveMessageCode() throws Exception {
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName");
		assertThat(codes).containsExactly(
				"errorCode.objectName",
				"errorCode");
	}

	@Test
	public void shouldResolveFieldMessageCode() throws Exception {
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName", "field",
				TestBean.class);
		assertThat(codes).containsExactly(
				"errorCode.objectName.field",
				"errorCode.field",
				"errorCode.org.springframework.beans.testfixture.beans.TestBean",
				"errorCode");
	}

	@Test
	public void shouldResolveIndexedFieldMessageCode() throws Exception {
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName", "a.b[3].c[5].d",
				TestBean.class);
		assertThat(codes).containsExactly(
				"errorCode.objectName.a.b[3].c[5].d",
				"errorCode.objectName.a.b[3].c.d",
				"errorCode.objectName.a.b.c.d",
				"errorCode.a.b[3].c[5].d",
				"errorCode.a.b[3].c.d",
				"errorCode.a.b.c.d",
				"errorCode.d",
				"errorCode.org.springframework.beans.testfixture.beans.TestBean",
				"errorCode");
	}

	@Test
	public void shouldResolveMessageCodeWithPrefix() throws Exception {
		resolver.setPrefix("prefix.");
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName");
		assertThat(codes).containsExactly(
				"prefix.errorCode.objectName",
				"prefix.errorCode");
	}

	@Test
	public void shouldResolveFieldMessageCodeWithPrefix() throws Exception {
		resolver.setPrefix("prefix.");
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName", "field",
				TestBean.class);
		assertThat(codes).containsExactly(
				"prefix.errorCode.objectName.field",
				"prefix.errorCode.field",
				"prefix.errorCode.org.springframework.beans.testfixture.beans.TestBean",
				"prefix.errorCode");
	}

	@Test
	public void shouldSupportNullPrefix() throws Exception {
		resolver.setPrefix(null);
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName", "field",
				TestBean.class);
		assertThat(codes).containsExactly(
				"errorCode.objectName.field",
				"errorCode.field",
				"errorCode.org.springframework.beans.testfixture.beans.TestBean",
				"errorCode");
	}

	@Test
	public void shouldSupportMalformedIndexField() throws Exception {
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName", "field[",
				TestBean.class);
		assertThat(codes).containsExactly(
				"errorCode.objectName.field[",
				"errorCode.field[",
				"errorCode.org.springframework.beans.testfixture.beans.TestBean",
				"errorCode");
	}

	@Test
	public void shouldSupportNullFieldType() throws Exception {
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName", "field",
				null);
		assertThat(codes).containsExactly(
				"errorCode.objectName.field",
				"errorCode.field",
				"errorCode");
	}

	@Test
	public void shouldSupportPostfixFormat() throws Exception {
		resolver.setMessageCodeFormatter(Format.POSTFIX_ERROR_CODE);
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName");
		assertThat(codes).containsExactly(
				"objectName.errorCode",
				"errorCode");
	}

	@Test
	public void shouldSupportFieldPostfixFormat() throws Exception {
		resolver.setMessageCodeFormatter(Format.POSTFIX_ERROR_CODE);
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName", "field",
				TestBean.class);
		assertThat(codes).containsExactly(
				"objectName.field.errorCode",
				"field.errorCode",
				"org.springframework.beans.testfixture.beans.TestBean.errorCode",
				"errorCode");
	}

	@Test
	public void shouldSupportCustomFormat() throws Exception {
		resolver.setMessageCodeFormatter(new MessageCodeFormatter() {
			@Override
			public String format(String errorCode, String objectName, String field) {
				return DefaultMessageCodesResolver.Format.toDelimitedString(
						"CUSTOM-" + errorCode, objectName, field);
			}
		});
		String[] codes = resolver.resolveMessageCodes("errorCode", "objectName");
		assertThat(codes).containsExactly(
				"CUSTOM-errorCode.objectName",
				"CUSTOM-errorCode");
	}

}
