package org.springframework.oxm.jibx;

import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;

import org.springframework.core.testfixture.xml.XmlContent;
import org.springframework.oxm.AbstractMarshallerTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.condition.JRE.JAVA_8;

/**
 * NOTE: These tests fail under Eclipse/IDEA because JiBX binding does not occur by
 * default. The Gradle build should succeed, however.
 *
 * @author Arjen Poutsma
 * @author Sam Brannen
 */
@Deprecated
@EnabledOnJre(JAVA_8) // JiBX compiler is currently not compatible with JDK 9
public class JibxMarshallerTests extends AbstractMarshallerTests<JibxMarshaller> {

	@Override
	protected JibxMarshaller createMarshaller() throws Exception {
		JibxMarshaller marshaller = new JibxMarshaller();
		marshaller.setTargetPackage("org.springframework.oxm.jibx");
		marshaller.afterPropertiesSet();
		return marshaller;
	}

	@Override
	protected Object createFlights() {
		Flights flights = new Flights();
		FlightType flight = new FlightType();
		flight.setNumber(42L);
		flights.addFlight(flight);
		return flights;
	}


	@Test
	public void afterPropertiesSetNoContextPath() throws Exception {
		JibxMarshaller marshaller = new JibxMarshaller();
		assertThatIllegalArgumentException().isThrownBy(
				marshaller::afterPropertiesSet);
	}

	@Test
	public void indentation() throws Exception {
		marshaller.setIndent(4);
		StringWriter writer = new StringWriter();
		marshaller.marshal(flights, new StreamResult(writer));
		String expected =
				"<?xml version=\"1.0\"?>\n" + "<flights xmlns=\"http://samples.springframework.org/flight\">\n" +
						"    <flight>\n" + "        <number>42</number>\n" + "    </flight>\n" + "</flights>";
		assertThat(XmlContent.from(writer)).isSimilarToIgnoringWhitespace(expected);
	}

	@Test
	public void encodingAndStandalone() throws Exception {
		marshaller.setEncoding("ISO-8859-1");
		marshaller.setStandalone(Boolean.TRUE);
		StringWriter writer = new StringWriter();
		marshaller.marshal(flights, new StreamResult(writer));
		assertThat(writer.toString().startsWith("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>")).as("Encoding and standalone not set").isTrue();
	}

	@Test
	public void dtd() throws Exception {
		marshaller.setDocTypeRootElementName("flights");
		marshaller.setDocTypeSystemId("flights.dtd");
		StringWriter writer = new StringWriter();
		marshaller.marshal(flights, new StreamResult(writer));
		assertThat(writer.toString().contains("<!DOCTYPE flights SYSTEM \"flights.dtd\">")).as("doc type not written").isTrue();
	}

	@Test
	public void supports() throws Exception {
		assertThat(marshaller.supports(Flights.class)).as("JibxMarshaller does not support Flights").isTrue();
		assertThat(marshaller.supports(FlightType.class)).as("JibxMarshaller does not support FlightType").isTrue();
		assertThat(marshaller.supports(getClass())).as("JibxMarshaller supports illegal type").isFalse();
	}

}
