package org.springframework.core.type;

/**
 * Tests for {@link StandardAnnotationMetadata}.
 *
 * @author Phillip Webb
 */
class StandardAnnotationMetadataTests extends AbstractAnnotationMetadataTests {

	@Override
	@SuppressWarnings("deprecation")
	protected AnnotationMetadata get(Class<?> source) {
		return new StandardAnnotationMetadata(source);
	}

}
