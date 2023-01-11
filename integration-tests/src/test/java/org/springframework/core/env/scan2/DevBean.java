package org.springframework.core.env.scan2;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(org.springframework.core.env.EnvironmentSystemIntegrationTests.Constants.DEV_ENV_NAME)
@Component(org.springframework.core.env.EnvironmentSystemIntegrationTests.Constants.DEV_BEAN_NAME)
class DevBean {
}
