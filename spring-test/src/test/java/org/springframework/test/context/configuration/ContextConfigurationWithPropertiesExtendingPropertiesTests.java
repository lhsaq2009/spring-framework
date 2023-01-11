package org.springframework.test.context.configuration;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.testfixture.beans.Pet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.junit4.PropertiesBasedSpringJUnit4ClassRunnerAppCtxTests;
import org.springframework.test.context.support.GenericPropertiesContextLoader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests which verify that the same custom {@link ContextLoader} can
 * be used at all levels within a test class hierarchy when the
 * {@code loader} is explicitly declared via {@link ContextConfiguration
 * &#064;ContextConfiguration}.
 *
 * @author Sam Brannen
 * @since 3.0
 * @see PropertiesBasedSpringJUnit4ClassRunnerAppCtxTests
 * @see ContextConfigurationWithPropertiesExtendingPropertiesAndInheritedLoaderTests
 */
@ContextConfiguration(loader = GenericPropertiesContextLoader.class)
public class ContextConfigurationWithPropertiesExtendingPropertiesTests extends
		PropertiesBasedSpringJUnit4ClassRunnerAppCtxTests {

	@Autowired
	private Pet dog;

	@Autowired
	private String testString2;


	@Test
	public void verifyExtendedAnnotationAutowiredFields() {
		assertThat(this.dog).as("The dog field should have been autowired.").isNotNull();
		assertThat(this.dog.getName()).isEqualTo("Fido");

		assertThat(this.testString2).as("The testString2 field should have been autowired.").isNotNull();
		assertThat(this.testString2).isEqualTo("Test String #2");
	}

}
