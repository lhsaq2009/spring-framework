package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link CharArrayPropertyEditor} class.
 *
 * @author Rick Evans
 */
public class CharArrayPropertyEditorTests {

	private final PropertyEditor charEditor = new CharArrayPropertyEditor();

	@Test
	public void sunnyDaySetAsText() throws Exception {
		final String text = "Hideous towns make me throw... up";
		charEditor.setAsText(text);

		Object value = charEditor.getValue();
		assertThat(value).isNotNull().isInstanceOf(char[].class);
		char[] chars = (char[]) value;
		for (int i = 0; i < text.length(); ++i) {
			assertThat(chars[i]).as("char[] differs at index '" + i + "'").isEqualTo(text.charAt(i));
		}
		assertThat(charEditor.getAsText()).isEqualTo(text);
	}

	@Test
	public void getAsTextReturnsEmptyStringIfValueIsNull() throws Exception {
		assertThat(charEditor.getAsText()).isEqualTo("");

		charEditor.setAsText(null);
		assertThat(charEditor.getAsText()).isEqualTo("");
	}

}
