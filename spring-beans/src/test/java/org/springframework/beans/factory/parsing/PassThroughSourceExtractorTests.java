package org.springframework.beans.factory.parsing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link PassThroughSourceExtractor}.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class PassThroughSourceExtractorTests {

	@Test
	public void testPassThroughContract() throws Exception {
		Object source  = new Object();
		Object extractedSource = new PassThroughSourceExtractor().extractSource(source, null);
		assertThat(extractedSource).as("The contract of PassThroughSourceExtractor states that the supplied " +
				"source object *must* be returned as-is").isSameAs(source);
	}

	@Test
	public void testPassThroughContractEvenWithNull() throws Exception {
		Object extractedSource = new PassThroughSourceExtractor().extractSource(null, null);
		assertThat(extractedSource).as("The contract of PassThroughSourceExtractor states that the supplied " +
				"source object *must* be returned as-is (even if null)").isNull();
	}

}
