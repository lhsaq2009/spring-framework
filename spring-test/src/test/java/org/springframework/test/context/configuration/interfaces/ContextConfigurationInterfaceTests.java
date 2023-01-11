package org.springframework.test.context.configuration.interfaces;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.testfixture.beans.Employee;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 4.3
 */
@ExtendWith(SpringExtension.class)
class ContextConfigurationInterfaceTests implements ContextConfigurationTestInterface {

	@Autowired
	Employee employee;


	@Test
	void profileFromTestInterface() {
		assertThat(employee).isNotNull();
		assertThat(employee.getName()).isEqualTo("Dilbert");
	}

}
