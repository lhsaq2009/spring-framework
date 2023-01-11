package org.springframework.test.context.junit4.aci.annotation;

import org.junit.Test;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.aci.DevProfileInitializer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests that verify support for {@link ApplicationContextInitializer
 * ApplicationContextInitializers} in conjunction with annotation-driven
 * configuration in the TestContext framework.
 *
 * @author Sam Brannen
 * @since 3.2
 */
@ContextConfiguration(initializers = DevProfileInitializer.class, inheritInitializers = false)
public class OverriddenInitializersAnnotationConfigTests extends SingleInitializerAnnotationConfigTests {

	@Test
	@Override
	public void activeBeans() {
		assertThat(foo).isEqualTo("foo");
		assertThat(bar).isNull();
		assertThat(baz).isEqualTo("dev profile config");
	}
}
