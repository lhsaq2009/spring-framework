package org.springframework.beans.factory.parsing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rick Evans
 * @author Chris Beams
 */
public class NullSourceExtractorTests {

	@Test
	public void testPassThroughContract() throws Exception {
		Object source  = new Object();
		Object extractedSource = new NullSourceExtractor().extractSource(source, null);
		assertThat(extractedSource).as("The contract of NullSourceExtractor states that the extraction *always* return null").isNull();
	}

	@Test
	public void testPassThroughContractEvenWithNull() throws Exception {
		Object extractedSource = new NullSourceExtractor().extractSource(null, null);
		assertThat(extractedSource).as("The contract of NullSourceExtractor states that the extraction *always* return null").isNull();
	}

}
