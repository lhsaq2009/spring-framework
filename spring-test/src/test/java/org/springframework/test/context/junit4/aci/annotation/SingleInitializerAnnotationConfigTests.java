package org.springframework.test.context.junit4.aci.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.aci.FooBarAliasInitializer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests that verify support for {@link ApplicationContextInitializer
 * ApplicationContextInitializers} in conjunction with annotation-driven
 * configuration in the TestContext framework.
 *
 * @author Sam Brannen
 * @since 3.2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { GlobalConfig.class, DevProfileConfig.class }, initializers = FooBarAliasInitializer.class)
public class SingleInitializerAnnotationConfigTests {

	@Autowired
	protected String foo;

	@Autowired(required = false)
	@Qualifier("bar")
	protected String bar;

	@Autowired
	protected String baz;


	@Test
	public void activeBeans() {
		assertThat(foo).isEqualTo("foo");
		assertThat(bar).isEqualTo("foo");
		assertThat(baz).isEqualTo("global config");
	}

}
