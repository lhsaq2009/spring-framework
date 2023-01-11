package org.springframework.scheduling.support;

import java.util.Date;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Juergen Hoeller
 * @author Ruslan Sibgatullin
 */
@SuppressWarnings("deprecation")
public class CronSequenceGeneratorTests {

	@Test
	public void at50Seconds() {
		assertThat(new CronSequenceGenerator("*/15 * 1-4 * * *").next(new Date(2012, 6, 1, 9, 53, 50))).isEqualTo(new Date(2012, 6, 2, 1, 0));
	}

	@Test
	public void at0Seconds() {
		assertThat(new CronSequenceGenerator("*/15 * 1-4 * * *").next(new Date(2012, 6, 1, 9, 53))).isEqualTo(new Date(2012, 6, 2, 1, 0));
	}

	@Test
	public void at0Minutes() {
		assertThat(new CronSequenceGenerator("0 */2 1-4 * * *").next(new Date(2012, 6, 1, 9, 0))).isEqualTo(new Date(2012, 6, 2, 1, 0));
	}

	@Test
	public void with0Increment() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new CronSequenceGenerator("*/0 * * * * *").next(new Date(2012, 6, 1, 9, 0)));
	}

	@Test
	public void withNegativeIncrement() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new CronSequenceGenerator("*/-1 * * * * *").next(new Date(2012, 6, 1, 9, 0)));
	}

	@Test
	public void withInvertedMinuteRange() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new CronSequenceGenerator("* 6-5 * * * *").next(new Date(2012, 6, 1, 9, 0)));
	}

	@Test
	public void withInvertedHourRange() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new CronSequenceGenerator("* * 6-5 * * *").next(new Date(2012, 6, 1, 9, 0)));
	}

	@Test
	public void withSameMinuteRange() {
		new CronSequenceGenerator("* 6-6 * * * *").next(new Date(2012, 6, 1, 9, 0));
	}

	@Test
	public void withSameHourRange() {
		new CronSequenceGenerator("* * 6-6 * * *").next(new Date(2012, 6, 1, 9, 0));
	}

	@Test
	public void validExpression() {
		assertThat(CronSequenceGenerator.isValidExpression("0 */2 1-4 * * *")).isTrue();
	}

	@Test
	public void invalidExpressionWithLength() {
		assertThat(CronSequenceGenerator.isValidExpression("0 */2 1-4 * * * *")).isFalse();
	}

	@Test
	public void invalidExpressionWithSeconds() {
		assertThat(CronSequenceGenerator.isValidExpression("100 */2 1-4 * * *")).isFalse();
	}

	@Test
	public void invalidExpressionWithMonths() {
		assertThat(CronSequenceGenerator.isValidExpression("0 */2 1-4 * INVALID *")).isFalse();
	}

	@Test
	public void nullExpression() {
		assertThat(CronSequenceGenerator.isValidExpression(null)).isFalse();
	}

}
