package org.springframework.beans.propertyeditors;

import java.io.Reader;

import org.junit.jupiter.api.Test;

import org.springframework.util.ClassUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for the {@link ReaderEditor} class.
 *
 * @author Juergen Hoeller
 * @since 4.2
 */
public class ReaderEditorTests {

	@Test
	public void testCtorWithNullResourceEditor() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ReaderEditor(null));
	}

	@Test
	public void testSunnyDay() throws Exception {
		Reader reader = null;
		try {
			String resource = "classpath:" + ClassUtils.classPackageAsResourcePath(getClass()) +
					"/" + ClassUtils.getShortName(getClass()) + ".class";
			ReaderEditor editor = new ReaderEditor();
			editor.setAsText(resource);
			Object value = editor.getValue();
			assertThat(value).isNotNull();
			boolean condition = value instanceof Reader;
			assertThat(condition).isTrue();
			reader = (Reader) value;
			assertThat(reader.ready()).isTrue();
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	@Test
	public void testWhenResourceDoesNotExist() throws Exception {
		String resource = "classpath:bingo!";
		ReaderEditor editor = new ReaderEditor();
		assertThatIllegalArgumentException().isThrownBy(() ->
				editor.setAsText(resource));
	}

	@Test
	public void testGetAsTextReturnsNullByDefault() throws Exception {
		assertThat(new ReaderEditor().getAsText()).isNull();
		String resource = "classpath:" + ClassUtils.classPackageAsResourcePath(getClass()) +
				"/" + ClassUtils.getShortName(getClass()) + ".class";
		ReaderEditor editor = new ReaderEditor();
		editor.setAsText(resource);
		assertThat(editor.getAsText()).isNull();
	}

}
