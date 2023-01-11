package org.springframework.web.multipart.support;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Rick Evans
 * @author Sam Brannen
 */
public class ByteArrayMultipartFileEditorTests {

	private final ByteArrayMultipartFileEditor editor = new ByteArrayMultipartFileEditor();

	@Test
	public void setValueAsByteArray() throws Exception {
		String expectedValue = "Shumwere, shumhow, a shuck ish washing you. - Drunken Far Side";
		editor.setValue(expectedValue.getBytes());
		assertThat(editor.getAsText()).isEqualTo(expectedValue);
	}

	@Test
	public void setValueAsString() throws Exception {
		String expectedValue = "'Green Wing' - classic British comedy";
		editor.setValue(expectedValue);
		assertThat(editor.getAsText()).isEqualTo(expectedValue);
	}

	@Test
	public void setValueAsCustomObjectInvokesToString() throws Exception {
		final String expectedValue = "'Green Wing' - classic British comedy";
		Object object = new Object() {
			@Override
			public String toString() {
				return expectedValue;
			}
		};

		editor.setValue(object);
		assertThat(editor.getAsText()).isEqualTo(expectedValue);
	}

	@Test
	public void setValueAsNullGetsBackEmptyString() throws Exception {
		editor.setValue(null);
		assertThat(editor.getAsText()).isEqualTo("");
	}

	@Test
	public void setValueAsMultipartFile() throws Exception {
		String expectedValue = "That is comforting to know";
		MultipartFile file = mock(MultipartFile.class);
		given(file.getBytes()).willReturn(expectedValue.getBytes());
		editor.setValue(file);
		assertThat(editor.getAsText()).isEqualTo(expectedValue);
	}

	@Test
	public void setValueAsMultipartFileWithBadBytes() throws Exception {
		MultipartFile file = mock(MultipartFile.class);
		given(file.getBytes()).willThrow(new IOException());
		assertThatIllegalArgumentException().isThrownBy(() ->
				editor.setValue(file));
	}

}
