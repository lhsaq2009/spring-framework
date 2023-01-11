package org.springframework.messaging.converter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmlunit.diff.DifferenceEvaluator;

import org.springframework.core.testfixture.xml.XmlContent;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.xmlunit.diff.ComparisonType.XML_STANDALONE;
import static org.xmlunit.diff.DifferenceEvaluators.Default;
import static org.xmlunit.diff.DifferenceEvaluators.chain;
import static org.xmlunit.diff.DifferenceEvaluators.downgradeDifferencesToEqual;

/**
 * @author Arjen Poutsma
 */
public class MarshallingMessageConverterTests {

	private MarshallingMessageConverter converter;


	@BeforeEach
	public void createMarshaller() throws Exception {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(MyBean.class);
		marshaller.afterPropertiesSet();

		this.converter = new MarshallingMessageConverter(marshaller);
	}


	@Test
	public void fromMessage() throws Exception {
		String payload = "<myBean><name>Foo</name></myBean>";
		Message<?> message = MessageBuilder.withPayload(payload.getBytes(StandardCharsets.UTF_8)).build();
		MyBean actual = (MyBean) this.converter.fromMessage(message, MyBean.class);

		assertThat(actual).isNotNull();
		assertThat(actual.getName()).isEqualTo("Foo");
	}

	@Test
	public void fromMessageInvalidXml() throws Exception {
		String payload = "<myBean><name>Foo</name><myBean>";
		Message<?> message = MessageBuilder.withPayload(payload.getBytes(StandardCharsets.UTF_8)).build();
		assertThatExceptionOfType(MessageConversionException.class).isThrownBy(() ->
				this.converter.fromMessage(message, MyBean.class));
	}

	@Test
	public void fromMessageValidXmlWithUnknownProperty() throws IOException {
		String payload = "<myBean><age>42</age><myBean>";
		Message<?> message = MessageBuilder.withPayload(payload.getBytes(StandardCharsets.UTF_8)).build();
		assertThatExceptionOfType(MessageConversionException.class).isThrownBy(() ->
				this.converter.fromMessage(message, MyBean.class));
	}

	@Test
	public void toMessage() throws Exception {
		MyBean payload = new MyBean();
		payload.setName("Foo");

		Message<?> message = this.converter.toMessage(payload, null);
		assertThat(message).isNotNull();
		String actual = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);

		DifferenceEvaluator ev = chain(Default, downgradeDifferencesToEqual(XML_STANDALONE));
		assertThat(XmlContent.of(actual)).isSimilarTo("<myBean><name>Foo</name></myBean>", ev);
	}


	@XmlRootElement
	public static class MyBean {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
