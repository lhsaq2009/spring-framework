package org.springframework.beans.factory.parsing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for {@link ConstructorArgumentEntry}.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class ConstructorArgumentEntryTests {

	@Test
	public void testCtorBailsOnNegativeCtorIndexArgument() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ConstructorArgumentEntry(-1));
	}

}
