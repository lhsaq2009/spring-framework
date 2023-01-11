package org.springframework.web.socket.adapter.standard;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.core.testfixture.security.TestPrincipal;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit tests for {@link org.springframework.web.socket.adapter.standard.StandardWebSocketSession}.
 *
 * @author Rossen Stoyanchev
 */
public class StandardWebSocketSessionTests {

	private final HttpHeaders headers = new HttpHeaders();

	private final Map<String, Object> attributes = new HashMap<>();


	@Test
	@SuppressWarnings("resource")
	public void getPrincipalWithConstructorArg() {
		TestPrincipal user = new TestPrincipal("joe");
		StandardWebSocketSession session = new StandardWebSocketSession(this.headers, this.attributes, null, null, user);

		assertThat(session.getPrincipal()).isSameAs(user);
	}

	@Test
	@SuppressWarnings("resource")
	public void getPrincipalWithNativeSession() {
		TestPrincipal user = new TestPrincipal("joe");

		Session nativeSession = Mockito.mock(Session.class);
		given(nativeSession.getUserPrincipal()).willReturn(user);

		StandardWebSocketSession session = new StandardWebSocketSession(this.headers, this.attributes, null, null);
		session.initializeNativeSession(nativeSession);

		assertThat(session.getPrincipal()).isSameAs(user);
	}

	@Test
	@SuppressWarnings("resource")
	public void getPrincipalNone() {
		Session nativeSession = Mockito.mock(Session.class);
		given(nativeSession.getUserPrincipal()).willReturn(null);

		StandardWebSocketSession session = new StandardWebSocketSession(this.headers, this.attributes, null, null);
		session.initializeNativeSession(nativeSession);

		reset(nativeSession);

		assertThat(session.getPrincipal()).isNull();
		verifyNoMoreInteractions(nativeSession);
	}

	@Test
	@SuppressWarnings("resource")
	public void getAcceptedProtocol() {
		String protocol = "foo";

		Session nativeSession = Mockito.mock(Session.class);
		given(nativeSession.getNegotiatedSubprotocol()).willReturn(protocol);

		StandardWebSocketSession session = new StandardWebSocketSession(this.headers, this.attributes, null, null);
		session.initializeNativeSession(nativeSession);

		reset(nativeSession);

		assertThat(session.getAcceptedProtocol()).isEqualTo(protocol);
		verifyNoMoreInteractions(nativeSession);
	}

}
