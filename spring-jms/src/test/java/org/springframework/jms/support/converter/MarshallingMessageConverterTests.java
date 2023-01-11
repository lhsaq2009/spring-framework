package org.springframework.jms.support.converter;

import javax.jms.BytesMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Arjen Poutsma
 */
public class MarshallingMessageConverterTests {

	private MarshallingMessageConverter converter;

	private Marshaller marshallerMock;

	private Unmarshaller unmarshallerMock;

	private Session sessionMock;


	@BeforeEach
	public void setup() throws Exception {
		marshallerMock = mock(Marshaller.class);
		unmarshallerMock = mock(Unmarshaller.class);
		sessionMock = mock(Session.class);
		converter = new MarshallingMessageConverter(marshallerMock, unmarshallerMock);
	}


	@Test
	public void toBytesMessage() throws Exception {
		BytesMessage bytesMessageMock = mock(BytesMessage.class);
		Object toBeMarshalled = new Object();
		given(sessionMock.createBytesMessage()).willReturn(bytesMessageMock);

		converter.toMessage(toBeMarshalled, sessionMock);

		verify(marshallerMock).marshal(eq(toBeMarshalled), isA(Result.class));
		verify(bytesMessageMock).writeBytes(isA(byte[].class));
	}

	@Test
	public void fromBytesMessage() throws Exception {
		BytesMessage bytesMessageMock = mock(BytesMessage.class);
		Object unmarshalled = new Object();

		given(bytesMessageMock.getBodyLength()).willReturn(10L);
		given(bytesMessageMock.readBytes(isA(byte[].class))).willReturn(0);
		given(unmarshallerMock.unmarshal(isA(Source.class))).willReturn(unmarshalled);

		Object result = converter.fromMessage(bytesMessageMock);
		assertThat(unmarshalled).as("Invalid result").isEqualTo(result);
	}

	@Test
	public void toTextMessage() throws Exception {
		converter.setTargetType(MessageType.TEXT);
		TextMessage textMessageMock = mock(TextMessage.class);
		Object toBeMarshalled = new Object();

		given(sessionMock.createTextMessage(isA(String.class))).willReturn(textMessageMock);

		converter.toMessage(toBeMarshalled, sessionMock);

		verify(marshallerMock).marshal(eq(toBeMarshalled), isA(Result.class));
	}

	@Test
	public void fromTextMessage() throws Exception {
		TextMessage textMessageMock = mock(TextMessage.class);
		Object unmarshalled = new Object();

		String text = "foo";
		given(textMessageMock.getText()).willReturn(text);
		given(unmarshallerMock.unmarshal(isA(Source.class))).willReturn(unmarshalled);

		Object result = converter.fromMessage(textMessageMock);
		assertThat(unmarshalled).as("Invalid result").isEqualTo(result);
	}

}
