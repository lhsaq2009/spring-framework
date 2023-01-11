package org.springframework.oxm.config;

import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link OxmNamespaceHandler} class.
 *
 * @author Arjen Poustma
 * @author Jakub Narloch
 * @author Sam Brannen
 */
public class OxmNamespaceHandlerTests {

	private final ApplicationContext applicationContext =
			new ClassPathXmlApplicationContext("oxmNamespaceHandlerTest.xml", getClass());


	@Test
	public void jaxb2ContextPathMarshaller() {
		Jaxb2Marshaller jaxb2Marshaller = applicationContext.getBean("jaxb2ContextPathMarshaller", Jaxb2Marshaller.class);
		assertThat(jaxb2Marshaller).isNotNull();
	}

	@Test
	public void jaxb2ClassesToBeBoundMarshaller() {
		Jaxb2Marshaller jaxb2Marshaller = applicationContext.getBean("jaxb2ClassesMarshaller", Jaxb2Marshaller.class);
		assertThat(jaxb2Marshaller).isNotNull();
	}

}
