package org.springframework.beans.propertyeditors;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import org.springframework.util.ClassUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for the {@link InputStreamEditor} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class InputStreamEditorTests {

	@Test
	public void testCtorWithNullResourceEditor() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new InputStreamEditor(null));
	}

	@Test
	public void testSunnyDay() throws Exception {
		InputStream stream = null;
		try {
			String resource = "classpath:" + ClassUtils.classPackageAsResourcePath(getClass()) +
					"/" + ClassUtils.getShortName(getClass()) + ".class";
			InputStreamEditor editor = new InputStreamEditor();
			editor.setAsText(resource);
			Object value = editor.getValue();
			assertThat(value).isNotNull();
			boolean condition = value instanceof InputStream;
			assertThat(condition).isTrue();
			stream = (InputStream) value;
			assertThat(stream.available() > 0).isTrue();
		}
		finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	@Test
	public void testWhenResourceDoesNotExist() throws Exception {
		InputStreamEditor editor = new InputStreamEditor();
		assertThatIllegalArgumentException().isThrownBy(() ->
				editor.setAsText("classpath:bingo!"));
	}

	@Test
	public void testGetAsTextReturnsNullByDefault() throws Exception {
		assertThat(new InputStreamEditor().getAsText()).isNull();
		String resource = "classpath:" + ClassUtils.classPackageAsResourcePath(getClass()) +
				"/" + ClassUtils.getShortName(getClass()) + ".class";
		InputStreamEditor editor = new InputStreamEditor();
		editor.setAsText(resource);
		assertThat(editor.getAsText()).isNull();
	}

}
