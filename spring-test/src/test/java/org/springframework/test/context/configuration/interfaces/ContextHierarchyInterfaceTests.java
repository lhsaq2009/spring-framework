package org.springframework.test.context.configuration.interfaces;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 4.3
 */
@ExtendWith(SpringExtension.class)
class ContextHierarchyInterfaceTests implements ContextHierarchyTestInterface {

	@Autowired
	String foo;

	@Autowired
	String bar;

	@Autowired
	String baz;

	@Autowired
	ApplicationContext context;


	@Test
	void loadContextHierarchy() {
		assertThat(context).as("child ApplicationContext").isNotNull();
		assertThat(context.getParent()).as("parent ApplicationContext").isNotNull();
		assertThat(context.getParent().getParent()).as("grandparent ApplicationContext").isNull();
		assertThat(foo).isEqualTo("foo");
		assertThat(bar).isEqualTo("bar");
		assertThat(baz).isEqualTo("baz-child");
	}

}
