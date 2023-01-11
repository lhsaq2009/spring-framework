package org.springframework.core.env.scan1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(org.springframework.core.env.EnvironmentSystemIntegrationTests.Constants.DEV_ENV_NAME)
@Configuration
class DevConfig {

	@Bean
	public Object devBean() {
		return new Object();
	}

}
