package org.springframework.beans.factory.xml;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Costin Leau
 */
@TestInstance(Lifecycle.PER_CLASS)
class ComponentBeanDefinitionParserTests {

	private final DefaultListableBeanFactory bf = new DefaultListableBeanFactory();


	@BeforeAll
	void setUp() throws Exception {
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(
			new ClassPathResource("component-config.xml", ComponentBeanDefinitionParserTests.class));
	}

	@AfterAll
	void tearDown() {
		bf.destroySingletons();
	}

	@Test
	void testBionicBasic() {
		Component cp = getBionicFamily();
		assertThat("Bionic-1").isEqualTo(cp.getName());
	}

	@Test
	void testBionicFirstLevelChildren() {
		Component cp = getBionicFamily();
		List<Component> components = cp.getComponents();
		assertThat(2).isEqualTo(components.size());
		assertThat("Mother-1").isEqualTo(components.get(0).getName());
		assertThat("Rock-1").isEqualTo(components.get(1).getName());
	}

	@Test
	void testBionicSecondLevelChildren() {
		Component cp = getBionicFamily();
		List<Component> components = cp.getComponents().get(0).getComponents();
		assertThat(2).isEqualTo(components.size());
		assertThat("Karate-1").isEqualTo(components.get(0).getName());
		assertThat("Sport-1").isEqualTo(components.get(1).getName());
	}

	private Component getBionicFamily() {
		return bf.getBean("bionic-family", Component.class);
	}

}

