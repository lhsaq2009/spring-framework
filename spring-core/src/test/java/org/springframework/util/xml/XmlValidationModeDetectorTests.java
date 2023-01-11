package org.springframework.util.xml;

import java.io.InputStream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.xml.XmlValidationModeDetector.VALIDATION_DTD;

/**
 * Unit tests for {@link XmlValidationModeDetector}.
 *
 * @author Sam Brannen
 * @since 5.1.10
 */
class XmlValidationModeDetectorTests {

	private final XmlValidationModeDetector xmlValidationModeDetector = new XmlValidationModeDetector();


	@ParameterizedTest
	@ValueSource(strings = { "dtdWithTrailingComment.xml", "dtdWithLeadingComment.xml", "dtdWithCommentOnNextLine.xml",
		"dtdWithMultipleComments.xml" })
	void dtdDetection(String fileName) throws Exception {
		InputStream inputStream = getClass().getResourceAsStream(fileName);
		assertThat(xmlValidationModeDetector.detectValidationMode(inputStream)).isEqualTo(VALIDATION_DTD);
	}

}
