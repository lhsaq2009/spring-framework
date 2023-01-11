package org.springframework.core.env.scan1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(org.springframework.core.env.EnvironmentSystemIntegrationTests.Constants.PROD_ENV_NAME)
@Configuration
class ProdConfig {

	@Bean
	public Object prodBean() {
		return new Object();
	}

}
