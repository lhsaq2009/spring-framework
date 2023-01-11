package org.springframework.mail.javamail;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Brian Hanafee
 * @author Sam Brannen
 * @since 09.07.2005
 */
public class InternetAddressEditorTests {

	private static final String EMPTY = "";
	private static final String SIMPLE = "nobody@nowhere.com";
	private static final String BAD = "(";

	private final InternetAddressEditor editor = new InternetAddressEditor();


	@Test
	public void uninitialized() {
		assertThat(editor.getAsText()).as("Uninitialized editor did not return empty value string").isEqualTo(EMPTY);
	}

	@Test
	public void setNull() {
		editor.setAsText(null);
		assertThat(editor.getAsText()).as("Setting null did not result in empty value string").isEqualTo(EMPTY);
	}

	@Test
	public void setEmpty() {
		editor.setAsText(EMPTY);
		assertThat(editor.getAsText()).as("Setting empty string did not result in empty value string").isEqualTo(EMPTY);
	}

	@Test
	public void allWhitespace() {
		editor.setAsText(" ");
		assertThat(editor.getAsText()).as("All whitespace was not recognized").isEqualTo(EMPTY);
	}

	@Test
	public void simpleGoodAddress() {
		editor.setAsText(SIMPLE);
		assertThat(editor.getAsText()).as("Simple email address failed").isEqualTo(SIMPLE);
	}

	@Test
	public void excessWhitespace() {
		editor.setAsText(" " + SIMPLE + " ");
		assertThat(editor.getAsText()).as("Whitespace was not stripped").isEqualTo(SIMPLE);
	}

	@Test
	public void simpleBadAddress() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				editor.setAsText(BAD));
	}

}
