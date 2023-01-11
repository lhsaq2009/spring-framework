package org.springframework.test.context.configuration.interfaces;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 4.3
 */
@ExtendWith(SpringExtension.class)
class WebAppConfigurationInterfaceTests implements WebAppConfigurationTestInterface {

	@Autowired
	WebApplicationContext wac;


	@Test
	void wacLoaded() {
		assertThat(wac).isNotNull();
	}

}
