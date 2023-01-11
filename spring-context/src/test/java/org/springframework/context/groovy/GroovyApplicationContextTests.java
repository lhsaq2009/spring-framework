package org.springframework.context.groovy;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.context.support.GenericGroovyApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Jeff Brown
 * @author Juergen Hoeller
 */
public class GroovyApplicationContextTests {

	@Test
	public void testLoadingConfigFile() {
		GenericGroovyApplicationContext ctx = new GenericGroovyApplicationContext(
				"org/springframework/context/groovy/applicationContext.groovy");

		Object framework = ctx.getBean("framework");
		assertThat(framework).as("could not find framework bean").isNotNull();
		assertThat(framework).isEqualTo("Grails");
	}

	@Test
	public void testLoadingMultipleConfigFiles() {
		GenericGroovyApplicationContext ctx = new GenericGroovyApplicationContext(
				"org/springframework/context/groovy/applicationContext2.groovy",
				"org/springframework/context/groovy/applicationContext.groovy");

		Object framework = ctx.getBean("framework");
		assertThat(framework).as("could not find framework bean").isNotNull();
		assertThat(framework).isEqualTo("Grails");

		Object company = ctx.getBean("company");
		assertThat(company).as("could not find company bean").isNotNull();
		assertThat(company).isEqualTo("SpringSource");
	}

	@Test
	public void testLoadingMultipleConfigFilesWithRelativeClass() {
		GenericGroovyApplicationContext ctx = new GenericGroovyApplicationContext();
		ctx.load(GroovyApplicationContextTests.class, "applicationContext2.groovy", "applicationContext.groovy");
		ctx.refresh();

		Object framework = ctx.getBean("framework");
		assertThat(framework).as("could not find framework bean").isNotNull();
		assertThat(framework).isEqualTo("Grails");

		Object company = ctx.getBean("company");
		assertThat(company).as("could not find company bean").isNotNull();
		assertThat(company).isEqualTo("SpringSource");
	}

	@Test
	public void testConfigFileParsingError() {
		assertThatExceptionOfType(BeanDefinitionParsingException.class).isThrownBy(() ->
				new GenericGroovyApplicationContext("org/springframework/context/groovy/applicationContext-error.groovy"));
	}

}
