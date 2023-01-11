package org.springframework.instrument.classloading;

import java.io.IOException;
import java.util.Enumeration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rod Johnson
 * @author Chris Beams
 * @since 2.0
 */
public class ResourceOverridingShadowingClassLoaderTests {

	private static final String EXISTING_RESOURCE = "org/springframework/instrument/classloading/testResource.xml";

	private ClassLoader thisClassLoader = getClass().getClassLoader();

	private ResourceOverridingShadowingClassLoader overridingLoader = new ResourceOverridingShadowingClassLoader(thisClassLoader);


	@Test
	public void testFindsExistingResourceWithGetResourceAndNoOverrides() {
		assertThat(thisClassLoader.getResource(EXISTING_RESOURCE)).isNotNull();
		assertThat(overridingLoader.getResource(EXISTING_RESOURCE)).isNotNull();
	}

	@Test
	public void testDoesNotFindExistingResourceWithGetResourceAndNullOverride() {
		assertThat(thisClassLoader.getResource(EXISTING_RESOURCE)).isNotNull();
		overridingLoader.override(EXISTING_RESOURCE, null);
		assertThat(overridingLoader.getResource(EXISTING_RESOURCE)).isNull();
	}

	@Test
	public void testFindsExistingResourceWithGetResourceAsStreamAndNoOverrides() {
		assertThat(thisClassLoader.getResourceAsStream(EXISTING_RESOURCE)).isNotNull();
		assertThat(overridingLoader.getResourceAsStream(EXISTING_RESOURCE)).isNotNull();
	}

	@Test
	public void testDoesNotFindExistingResourceWithGetResourceAsStreamAndNullOverride() {
		assertThat(thisClassLoader.getResourceAsStream(EXISTING_RESOURCE)).isNotNull();
		overridingLoader.override(EXISTING_RESOURCE, null);
		assertThat(overridingLoader.getResourceAsStream(EXISTING_RESOURCE)).isNull();
	}

	@Test
	public void testFindsExistingResourceWithGetResourcesAndNoOverrides() throws IOException {
		assertThat(thisClassLoader.getResources(EXISTING_RESOURCE)).isNotNull();
		assertThat(overridingLoader.getResources(EXISTING_RESOURCE)).isNotNull();
		assertThat(countElements(overridingLoader.getResources(EXISTING_RESOURCE))).isEqualTo(1);
	}

	@Test
	public void testDoesNotFindExistingResourceWithGetResourcesAndNullOverride() throws IOException {
		assertThat(thisClassLoader.getResources(EXISTING_RESOURCE)).isNotNull();
		overridingLoader.override(EXISTING_RESOURCE, null);
		assertThat(countElements(overridingLoader.getResources(EXISTING_RESOURCE))).isEqualTo(0);
	}

	private int countElements(Enumeration<?> e) {
		int elts = 0;
		while (e.hasMoreElements()) {
			e.nextElement();
			++elts;
		}
		return elts;
	}
}
