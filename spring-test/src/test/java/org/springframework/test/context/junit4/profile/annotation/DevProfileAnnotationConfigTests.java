package org.springframework.test.context.junit4.profile.annotation;

import org.junit.Test;

import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.1
 */
@ActiveProfiles("dev")
public class DevProfileAnnotationConfigTests extends DefaultProfileAnnotationConfigTests {

	@Test
	@Override
	public void employee() {
		assertThat(employee).as("employee bean should be loaded for the 'dev' profile").isNotNull();
		assertThat(employee.getName()).isEqualTo("John Smith");
	}

}
