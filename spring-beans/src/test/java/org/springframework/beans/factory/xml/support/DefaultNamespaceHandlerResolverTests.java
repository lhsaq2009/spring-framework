package org.springframework.beans.factory.xml.support;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.UtilNamespaceHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit and integration tests for the {@link DefaultNamespaceHandlerResolver} class.
 *
 * @author Rob Harrop
 * @author Rick Evans
 */
public class DefaultNamespaceHandlerResolverTests {

	@Test
	public void testResolvedMappedHandler() {
		DefaultNamespaceHandlerResolver resolver = new DefaultNamespaceHandlerResolver(getClass().getClassLoader());
		NamespaceHandler handler = resolver.resolve("http://www.springframework.org/schema/util");
		assertThat(handler).as("Handler should not be null.").isNotNull();
		assertThat(handler.getClass()).as("Incorrect handler loaded").isEqualTo(UtilNamespaceHandler.class);
	}

	@Test
	public void testResolvedMappedHandlerWithNoArgCtor() {
		DefaultNamespaceHandlerResolver resolver = new DefaultNamespaceHandlerResolver();
		NamespaceHandler handler = resolver.resolve("http://www.springframework.org/schema/util");
		assertThat(handler).as("Handler should not be null.").isNotNull();
		assertThat(handler.getClass()).as("Incorrect handler loaded").isEqualTo(UtilNamespaceHandler.class);
	}

	@Test
	public void testNonExistentHandlerClass() {
		String mappingPath = "org/springframework/beans/factory/xml/support/nonExistent.properties";
		new DefaultNamespaceHandlerResolver(getClass().getClassLoader(), mappingPath);
	}

	@Test
	public void testCtorWithNullClassLoaderArgument() {
		// simply must not bail...
		new DefaultNamespaceHandlerResolver(null);
	}

	@Test
	public void testCtorWithNullClassLoaderArgumentAndNullMappingLocationArgument() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new DefaultNamespaceHandlerResolver(null, null));
	}

	@Test
	public void testCtorWithNonExistentMappingLocationArgument() {
		// simply must not bail; we don't want non-existent resources to result in an Exception
		new DefaultNamespaceHandlerResolver(null, "738trbc bobabloobop871");
	}

}
