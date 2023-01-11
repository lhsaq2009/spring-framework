package org.springframework.util;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 * @since 4.2
 */
class Base64UtilsTests {

	@Test
	void encode() throws UnsupportedEncodingException {
		byte[] bytes = new byte[]
				{-0x4f, 0xa, -0x73, -0x4f, 0x64, -0x20, 0x75, 0x41, 0x5, -0x49, -0x57, -0x65, -0x19, 0x2e, 0x3f, -0x1b};
		assertThat(Base64Utils.decode(Base64Utils.encode(bytes))).isEqualTo(bytes);

		bytes = "Hello World".getBytes("UTF-8");
		assertThat(Base64Utils.decode(Base64Utils.encode(bytes))).isEqualTo(bytes);

		bytes = "Hello World\r\nSecond Line".getBytes("UTF-8");
		assertThat(Base64Utils.decode(Base64Utils.encode(bytes))).isEqualTo(bytes);

		bytes = "Hello World\r\nSecond Line\r\n".getBytes("UTF-8");
		assertThat(Base64Utils.decode(Base64Utils.encode(bytes))).isEqualTo(bytes);

		bytes = new byte[] { (byte) 0xfb, (byte) 0xf0 };
		assertThat(Base64Utils.encode(bytes)).isEqualTo("+/A=".getBytes());
		assertThat(Base64Utils.decode(Base64Utils.encode(bytes))).isEqualTo(bytes);

		assertThat(Base64Utils.encodeUrlSafe(bytes)).isEqualTo("-_A=".getBytes());
		assertThat(Base64Utils.decodeUrlSafe(Base64Utils.encodeUrlSafe(bytes))).isEqualTo(bytes);
	}

	@Test
	void encodeToStringWithJdk8VsJaxb() throws UnsupportedEncodingException {
		byte[] bytes = new byte[]
				{-0x4f, 0xa, -0x73, -0x4f, 0x64, -0x20, 0x75, 0x41, 0x5, -0x49, -0x57, -0x65, -0x19, 0x2e, 0x3f, -0x1b};
		assertThat(DatatypeConverter.printBase64Binary(bytes)).isEqualTo(Base64Utils.encodeToString(bytes));
		assertThat(Base64Utils.decodeFromString(Base64Utils.encodeToString(bytes))).isEqualTo(bytes);
		assertThat(DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(bytes))).isEqualTo(bytes);

		bytes = "Hello World".getBytes("UTF-8");
		assertThat(DatatypeConverter.printBase64Binary(bytes)).isEqualTo(Base64Utils.encodeToString(bytes));
		assertThat(Base64Utils.decodeFromString(Base64Utils.encodeToString(bytes))).isEqualTo(bytes);
		assertThat(DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(bytes))).isEqualTo(bytes);

		bytes = "Hello World\r\nSecond Line".getBytes("UTF-8");
		assertThat(DatatypeConverter.printBase64Binary(bytes)).isEqualTo(Base64Utils.encodeToString(bytes));
		assertThat(Base64Utils.decodeFromString(Base64Utils.encodeToString(bytes))).isEqualTo(bytes);
		assertThat(DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(bytes))).isEqualTo(bytes);

		bytes = "Hello World\r\nSecond Line\r\n".getBytes("UTF-8");
		assertThat(DatatypeConverter.printBase64Binary(bytes)).isEqualTo(Base64Utils.encodeToString(bytes));
		assertThat(Base64Utils.decodeFromString(Base64Utils.encodeToString(bytes))).isEqualTo(bytes);
		assertThat(DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(bytes))).isEqualTo(bytes);
	}

	@Test
	void encodeDecodeUrlSafe() {
		byte[] bytes = new byte[] { (byte) 0xfb, (byte) 0xf0 };
		assertThat(Base64Utils.encodeUrlSafe(bytes)).isEqualTo("-_A=".getBytes());
		assertThat(Base64Utils.decodeUrlSafe(Base64Utils.encodeUrlSafe(bytes))).isEqualTo(bytes);

		assertThat(Base64Utils.encodeToUrlSafeString(bytes)).isEqualTo("-_A=");
		assertThat(Base64Utils.decodeFromUrlSafeString(Base64Utils.encodeToUrlSafeString(bytes))).isEqualTo(bytes);
	}

}
