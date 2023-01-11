package org.springframework.core.env.scan1;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ DevConfig.class, ProdConfig.class })
class Config {

}
