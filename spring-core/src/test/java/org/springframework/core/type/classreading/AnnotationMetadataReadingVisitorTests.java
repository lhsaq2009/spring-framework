package org.springframework.core.type.classreading;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import org.springframework.asm.ClassReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AbstractAnnotationMetadataTests;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Tests for {@link AnnotationMetadataReadingVisitor}.
 *
 * @author Phillip Webb
 */
@SuppressWarnings("deprecation")
class AnnotationMetadataReadingVisitorTests extends AbstractAnnotationMetadataTests {

	@Override
	protected AnnotationMetadata get(Class<?> source) {
		try {
			ClassLoader classLoader = source.getClassLoader();
			String className = source.getName();
			String resourcePath = ResourceLoader.CLASSPATH_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(className)
					+ ClassUtils.CLASS_FILE_SUFFIX;
			Resource resource = new DefaultResourceLoader().getResource(resourcePath);
			try (InputStream inputStream = new BufferedInputStream(
					resource.getInputStream())) {
				ClassReader classReader = new ClassReader(inputStream);
				AnnotationMetadataReadingVisitor metadata = new AnnotationMetadataReadingVisitor(
						classLoader);
				classReader.accept(metadata, ClassReader.SKIP_DEBUG);
				return metadata;
			}
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	@Test
	public void getAnnotationsReturnsDirectAnnotations() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(
				super::getAnnotationsReturnsDirectAnnotations);
	}

}
