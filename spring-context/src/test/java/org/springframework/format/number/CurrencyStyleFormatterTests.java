package org.springframework.format.number;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Keith Donald
 */
public class CurrencyStyleFormatterTests {

	private final CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();


	@Test
	public void formatValue() {
		assertThat(formatter.print(new BigDecimal("23"), Locale.US)).isEqualTo("$23.00");
	}

	@Test
	public void parseValue() throws ParseException {
		assertThat(formatter.parse("$23.56", Locale.US)).isEqualTo(new BigDecimal("23.56"));
	}

	@Test
	public void parseBogusValue() throws ParseException {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() ->
				formatter.parse("bogus", Locale.US));
	}

	@Test
	public void parseValueDefaultRoundDown() throws ParseException {
		this.formatter.setRoundingMode(RoundingMode.DOWN);
		assertThat(formatter.parse("$23.567", Locale.US)).isEqualTo(new BigDecimal("23.56"));
	}

	@Test
	public void parseWholeValue() throws ParseException {
		assertThat(formatter.parse("$23", Locale.US)).isEqualTo(new BigDecimal("23.00"));
	}

	@Test
	public void parseValueNotLenientFailure() throws ParseException {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() ->
				formatter.parse("$23.56bogus", Locale.US));
	}

}
