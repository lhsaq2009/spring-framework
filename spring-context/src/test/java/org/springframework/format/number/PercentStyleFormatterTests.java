package org.springframework.format.number;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Keith Donald
 */
public class PercentStyleFormatterTests {

	private final PercentStyleFormatter formatter = new PercentStyleFormatter();


	@Test
	public void formatValue() {
		assertThat(formatter.print(new BigDecimal(".23"), Locale.US)).isEqualTo("23%");
	}

	@Test
	public void parseValue() throws ParseException {
		assertThat(formatter.parse("23.56%", Locale.US)).isEqualTo(new BigDecimal(".2356"));
	}

	@Test
	public void parseBogusValue() throws ParseException {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() ->
				formatter.parse("bogus", Locale.US));
	}

	@Test
	public void parsePercentValueNotLenientFailure() throws ParseException {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() ->
				formatter.parse("23.56%bogus", Locale.US));
	}

}
