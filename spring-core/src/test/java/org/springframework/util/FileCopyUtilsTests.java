package org.springframework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the FileCopyUtils class.
 *
 * @author Juergen Hoeller
 * @since 12.03.2005
 */
class FileCopyUtilsTests {

	@Test
	void copyFromInputStream() throws IOException {
		byte[] content = "content".getBytes();
		ByteArrayInputStream in = new ByteArrayInputStream(content);
		ByteArrayOutputStream out = new ByteArrayOutputStream(content.length);
		int count = FileCopyUtils.copy(in, out);
		assertThat(count).isEqualTo(content.length);
		assertThat(Arrays.equals(content, out.toByteArray())).isTrue();
	}

	@Test
	void copyFromByteArray() throws IOException {
		byte[] content = "content".getBytes();
		ByteArrayOutputStream out = new ByteArrayOutputStream(content.length);
		FileCopyUtils.copy(content, out);
		assertThat(Arrays.equals(content, out.toByteArray())).isTrue();
	}

	@Test
	void copyToByteArray() throws IOException {
		byte[] content = "content".getBytes();
		ByteArrayInputStream in = new ByteArrayInputStream(content);
		byte[] result = FileCopyUtils.copyToByteArray(in);
		assertThat(Arrays.equals(content, result)).isTrue();
	}

	@Test
	void copyFromReader() throws IOException {
		String content = "content";
		StringReader in = new StringReader(content);
		StringWriter out = new StringWriter();
		int count = FileCopyUtils.copy(in, out);
		assertThat(count).isEqualTo(content.length());
		assertThat(out.toString()).isEqualTo(content);
	}

	@Test
	void copyFromString() throws IOException {
		String content = "content";
		StringWriter out = new StringWriter();
		FileCopyUtils.copy(content, out);
		assertThat(out.toString()).isEqualTo(content);
	}

	@Test
	void copyToString() throws IOException {
		String content = "content";
		StringReader in = new StringReader(content);
		String result = FileCopyUtils.copyToString(in);
		assertThat(result).isEqualTo(content);
	}

}
