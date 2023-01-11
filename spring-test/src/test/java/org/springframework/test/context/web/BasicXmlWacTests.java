package org.springframework.test.context.web;

import org.junit.Test;

import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.2
 */
@ContextConfiguration
public class BasicXmlWacTests extends AbstractBasicWacTests {

	@Test
	public void fooBarAutowired() {
		assertThat(foo).isEqualTo("bar");
	}

}
