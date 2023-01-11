package org.springframework.test.context.junit4.spr6128;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests to verify claims made in <a
 * href="https://jira.springframework.org/browse/SPR-6128"
 * target="_blank">SPR-6128</a>.
 *
 * @author Sam Brannen
 * @author Chris Beams
 * @since 3.0
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AutowiredQualifierTests {

	@Autowired
	private String foo;

	@Autowired
	@Qualifier("customFoo")
	private String customFoo;


	@Test
	public void test() {
		assertThat(foo).isEqualTo("normal");
		assertThat(customFoo).isEqualTo("custom");
	}

}
