package org.springframework.context.conversionservice;

import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Keith Donald
 */
public class ConversionServiceContextConfigTests {

	@Test
	public void testConfigOk() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/springframework/context/conversionservice/conversionService.xml");
		TestClient client = context.getBean("testClient", TestClient.class);
		assertThat(client.getBars().size()).isEqualTo(2);
		assertThat(client.getBars().get(0).getValue()).isEqualTo("value1");
		assertThat(client.getBars().get(1).getValue()).isEqualTo("value2");
		assertThat(client.isBool()).isTrue();
	}

}
