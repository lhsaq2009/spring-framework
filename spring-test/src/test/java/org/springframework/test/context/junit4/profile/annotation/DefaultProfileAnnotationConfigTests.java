package org.springframework.test.context.junit4.profile.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.testfixture.beans.Employee;
import org.springframework.beans.testfixture.beans.Pet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DefaultProfileConfig.class, DevProfileConfig.class }, loader = AnnotationConfigContextLoader.class)
public class DefaultProfileAnnotationConfigTests {

	@Autowired
	protected Pet pet;

	@Autowired(required = false)
	protected Employee employee;


	@Test
	public void pet() {
		assertThat(pet).isNotNull();
		assertThat(pet.getName()).isEqualTo("Fido");
	}

	@Test
	public void employee() {
		assertThat(employee).as("employee bean should not be created for the default profile").isNull();
	}

}
