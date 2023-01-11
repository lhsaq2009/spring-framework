package org.springframework.test.context.hierarchies.standard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.2.2
 */
@ExtendWith(SpringExtension.class)
@ContextHierarchy(@ContextConfiguration(name = "child", classes = ClassHierarchyWithMergedConfigLevelTwoTests.OrderConfig.class))
class ClassHierarchyWithMergedConfigLevelTwoTests extends ClassHierarchyWithMergedConfigLevelOneTests {

	@Configuration
	static class OrderConfig {

		@Autowired
		private ClassHierarchyWithMergedConfigLevelOneTests.UserConfig userConfig;

		@Bean
		String order() {
			return userConfig.user() + " + order";
		}
	}


	@Autowired
	private String order;


	@Test
	@Override
	void loadContextHierarchy() {
		super.loadContextHierarchy();
		assertThat(order).isEqualTo("parent + user + order");
	}

}
