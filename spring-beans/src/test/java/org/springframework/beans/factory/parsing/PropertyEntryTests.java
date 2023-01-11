package org.springframework.beans.factory.parsing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for {@link PropertyEntry}.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class PropertyEntryTests {

	@Test
	public void testCtorBailsOnNullPropertyNameArgument() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new PropertyEntry(null));
	}

	@Test
	public void testCtorBailsOnEmptyPropertyNameArgument() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new PropertyEntry(""));
	}

	@Test
	public void testCtorBailsOnWhitespacedPropertyNameArgument() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new PropertyEntry("\t   "));
	}

}
