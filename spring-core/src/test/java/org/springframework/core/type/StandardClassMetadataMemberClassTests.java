package org.springframework.core.type;

/**
 * @author Chris Beams
 * @since 3.1
 * @see AbstractClassMetadataMemberClassTests
 */
class StandardClassMetadataMemberClassTests extends AbstractClassMetadataMemberClassTests {

	@Override
	@SuppressWarnings("deprecation")
	public ClassMetadata getClassMetadataFor(Class<?> clazz) {
		return new StandardClassMetadata(clazz);
	}

}
