package org.springframework.core.type;

/**
 * Tests for {@link StandardMethodMetadata}.
 *
 * @author Phillip Webb
 */
class StandardMethodMetadataTests extends AbstractMethodMetadataTests {

	@Override
	protected AnnotationMetadata get(Class<?> source) {
		return AnnotationMetadata.introspect(source);
	}

}
