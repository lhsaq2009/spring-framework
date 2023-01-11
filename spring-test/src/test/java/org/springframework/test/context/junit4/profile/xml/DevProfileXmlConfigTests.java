package org.springframework.test.context.junit4.profile.xml;

import org.junit.Test;

import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.1
 */
@ActiveProfiles("dev")
public class DevProfileXmlConfigTests extends DefaultProfileXmlConfigTests {

	@Test
	@Override
	public void employee() {
		assertThat(employee).as("employee bean should be loaded for the 'dev' profile").isNotNull();
		assertThat(employee.getName()).isEqualTo("John Smith");
	}

}
