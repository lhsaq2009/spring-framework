package org.springframework.test.context.junit4.annotation;

import org.junit.Test;

import org.springframework.beans.testfixture.beans.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests that verify support for configuration classes in
 * the Spring TestContext Framework.
 *
 * <p>Configuration will be loaded from {@link DefaultConfigClassesBaseTests.ContextConfiguration}
 * and {@link BeanOverridingDefaultConfigClassesInheritedTests.ContextConfiguration}.
 *
 * @author Sam Brannen
 * @since 3.1
 */
@ContextConfiguration
public class BeanOverridingDefaultConfigClassesInheritedTests extends DefaultConfigClassesBaseTests {

	@Configuration
	static class ContextConfiguration {

		@Bean
		public Employee employee() {
			Employee employee = new Employee();
			employee.setName("Yoda");
			employee.setAge(900);
			employee.setCompany("The Force");
			return employee;
		}
	}


	@Test
	@Override
	public void verifyEmployeeSetFromBaseContextConfig() {
		assertThat(this.employee).as("The employee should have been autowired.").isNotNull();
		assertThat(this.employee.getName()).as("The employee bean should have been overridden.").isEqualTo("Yoda");
	}

}
