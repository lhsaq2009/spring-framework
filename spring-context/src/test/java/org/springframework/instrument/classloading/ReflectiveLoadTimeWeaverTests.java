package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Unit tests for the {@link ReflectiveLoadTimeWeaver} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class ReflectiveLoadTimeWeaverTests {

	@Test
	public void testCtorWithNullClassLoader() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ReflectiveLoadTimeWeaver(null));
	}

	@Test
	public void testCtorWithClassLoaderThatDoesNotExposeAnAddTransformerMethod() {
		assertThatIllegalStateException().isThrownBy(() ->
				new ReflectiveLoadTimeWeaver(getClass().getClassLoader()));
	}

	@Test
	public void testCtorWithClassLoaderThatDoesNotExposeAGetThrowawayClassLoaderMethodIsOkay() {
		JustAddTransformerClassLoader classLoader = new JustAddTransformerClassLoader();
		ReflectiveLoadTimeWeaver weaver = new ReflectiveLoadTimeWeaver(classLoader);
		weaver.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
				return "CAFEDEAD".getBytes();
			}
		});
		assertThat(classLoader.getNumTimesGetThrowawayClassLoaderCalled()).isEqualTo(1);
	}

	@Test
	public void testAddTransformerWithNullTransformer() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ReflectiveLoadTimeWeaver(new JustAddTransformerClassLoader()).addTransformer(null));
	}

	@Test
	public void testGetThrowawayClassLoaderWithClassLoaderThatDoesNotExposeAGetThrowawayClassLoaderMethodYieldsFallbackClassLoader() {
		ReflectiveLoadTimeWeaver weaver = new ReflectiveLoadTimeWeaver(new JustAddTransformerClassLoader());
		ClassLoader throwawayClassLoader = weaver.getThrowawayClassLoader();
		assertThat(throwawayClassLoader).isNotNull();
	}

	@Test
	public void testGetThrowawayClassLoaderWithTotallyCompliantClassLoader() {
		TotallyCompliantClassLoader classLoader = new TotallyCompliantClassLoader();
		ReflectiveLoadTimeWeaver weaver = new ReflectiveLoadTimeWeaver(classLoader);
		ClassLoader throwawayClassLoader = weaver.getThrowawayClassLoader();
		assertThat(throwawayClassLoader).isNotNull();
		assertThat(classLoader.getNumTimesGetThrowawayClassLoaderCalled()).isEqualTo(1);
	}


	public static class JustAddTransformerClassLoader extends ClassLoader {

		private int numTimesAddTransformerCalled = 0;


		public int getNumTimesGetThrowawayClassLoaderCalled() {
			return this.numTimesAddTransformerCalled;
		}


		public void addTransformer(ClassFileTransformer transformer) {
			++this.numTimesAddTransformerCalled;
		}

	}


	public static final class TotallyCompliantClassLoader extends JustAddTransformerClassLoader {

		private int numTimesGetThrowawayClassLoaderCalled = 0;


		@Override
		public int getNumTimesGetThrowawayClassLoaderCalled() {
			return this.numTimesGetThrowawayClassLoaderCalled;
		}


		public ClassLoader getThrowawayClassLoader() {
			++this.numTimesGetThrowawayClassLoaderCalled;
			return getClass().getClassLoader();
		}

	}

}
