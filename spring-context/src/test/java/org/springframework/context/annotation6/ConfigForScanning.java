package org.springframework.context.annotation6;

import org.springframework.beans.testfixture.beans.TestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigForScanning {
	@Bean
	public TestBean testBean() {
		return new TestBean();
	}
}
