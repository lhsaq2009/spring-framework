package org.springframework.aop.scope;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link DefaultScopedObject} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class DefaultScopedObjectTests {

	private static final String GOOD_BEAN_NAME = "foo";


	@Test
	public void testCtorWithNullBeanFactory() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
			new DefaultScopedObject(null, GOOD_BEAN_NAME));
	}

	@Test
	public void testCtorWithNullTargetBeanName() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				testBadTargetBeanName(null));
	}

	@Test
	public void testCtorWithEmptyTargetBeanName() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				testBadTargetBeanName(""));
	}

	@Test
	public void testCtorWithJustWhitespacedTargetBeanName() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				testBadTargetBeanName("   "));
	}

	private static void testBadTargetBeanName(final String badTargetBeanName) {
		ConfigurableBeanFactory factory = mock(ConfigurableBeanFactory.class);
		new DefaultScopedObject(factory, badTargetBeanName);
	}

}
