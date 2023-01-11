package org.springframework.beans.factory.xml;

import org.junit.jupiter.api.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for the {@link DelegatingEntityResolver} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class DelegatingEntityResolverTests {

	@Test
	public void testCtorWhereDtdEntityResolverIsNull() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new DelegatingEntityResolver(null, new NoOpEntityResolver()));
	}

	@Test
	public void testCtorWhereSchemaEntityResolverIsNull() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new DelegatingEntityResolver(new NoOpEntityResolver(), null));
	}

	@Test
	public void testCtorWhereEntityResolversAreBothNull() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new DelegatingEntityResolver(null, null));
	}


	private static final class NoOpEntityResolver implements EntityResolver {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) {
			return null;
		}
	}

}
