package org.springframework.core.type.classreading;

import org.springframework.core.type.AbstractMethodMetadataTests;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Tests for {@link SimpleMethodMetadata} and
 * {@link SimpleMethodMetadataReadingVisitor}.
 *
 * @author Phillip Webb
 */
class SimpleMethodMetadataTests extends AbstractMethodMetadataTests {

	@Override
	protected AnnotationMetadata get(Class<?> source) {
		try {
			return new SimpleMetadataReaderFactory(
					source.getClassLoader()).getMetadataReader(
							source.getName()).getAnnotationMetadata();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

}
