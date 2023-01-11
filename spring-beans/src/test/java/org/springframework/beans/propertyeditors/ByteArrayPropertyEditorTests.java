package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link ByteArrayPropertyEditor} class.
 *
 * @author Rick Evans
 */
public class ByteArrayPropertyEditorTests {

	private final PropertyEditor byteEditor = new ByteArrayPropertyEditor();

	@Test
	public void sunnyDaySetAsText() throws Exception {
		final String text = "Hideous towns make me throw... up";
		byteEditor.setAsText(text);

		Object value = byteEditor.getValue();
		assertThat(value).isNotNull().isInstanceOf(byte[].class);
		byte[] bytes = (byte[]) value;
		for (int i = 0; i < text.length(); ++i) {
			assertThat(bytes[i]).as("cyte[] differs at index '" + i + "'").isEqualTo((byte) text.charAt(i));
		}
		assertThat(byteEditor.getAsText()).isEqualTo(text);
	}

	@Test
	public void getAsTextReturnsEmptyStringIfValueIsNull() throws Exception {
		assertThat(byteEditor.getAsText()).isEqualTo("");

		byteEditor.setAsText(null);
		assertThat(byteEditor.getAsText()).isEqualTo("");
	}

}
