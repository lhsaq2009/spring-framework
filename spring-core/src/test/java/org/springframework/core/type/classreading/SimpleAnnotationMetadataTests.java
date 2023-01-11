package org.springframework.core.type.classreading;

import org.springframework.core.type.AbstractAnnotationMetadataTests;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Tests for {@link SimpleAnnotationMetadata} and
 * {@link SimpleAnnotationMetadataReadingVisitor}.
 *
 * @author Phillip Webb
 */
class SimpleAnnotationMetadataTests extends AbstractAnnotationMetadataTests {

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
