package org.springframework.instrument.classloading;

import org.junit.jupiter.api.Test;

import org.springframework.util.ClassUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Costin Leau
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class InstrumentableClassLoaderTests {

	@Test
	public void testDefaultLoadTimeWeaver() {
		ClassLoader loader = new SimpleInstrumentableClassLoader(ClassUtils.getDefaultClassLoader());
		ReflectiveLoadTimeWeaver handler = new ReflectiveLoadTimeWeaver(loader);
		assertThat(handler.getInstrumentableClassLoader()).isSameAs(loader);
	}

}
