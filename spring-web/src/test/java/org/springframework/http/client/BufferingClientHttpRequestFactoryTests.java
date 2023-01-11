package org.springframework.http.client;

import java.net.URI;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class BufferingClientHttpRequestFactoryTests extends AbstractHttpRequestFactoryTests {

	@Override
	protected ClientHttpRequestFactory createRequestFactory() {
		return new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}

	@Test
	public void repeatableRead() throws Exception {
		ClientHttpRequest request = factory.createRequest(new URI(baseUrl + "/echo"), HttpMethod.PUT);
		assertThat(request.getMethod()).as("Invalid HTTP method").isEqualTo(HttpMethod.PUT);
		String headerName = "MyHeader";
		String headerValue1 = "value1";
		request.getHeaders().add(headerName, headerValue1);
		String headerValue2 = "value2";
		request.getHeaders().add(headerName, headerValue2);
		byte[] body = "Hello World".getBytes("UTF-8");
		request.getHeaders().setContentLength(body.length);
		FileCopyUtils.copy(body, request.getBody());
		ClientHttpResponse response = request.execute();
		try {
			assertThat(response.getStatusCode()).as("Invalid status code").isEqualTo(HttpStatus.OK);
			assertThat(response.getStatusCode()).as("Invalid status code").isEqualTo(HttpStatus.OK);

			assertThat(response.getHeaders().containsKey(headerName)).as("Header not found").isTrue();
			assertThat(response.getHeaders().containsKey(headerName)).as("Header not found").isTrue();

			assertThat(response.getHeaders().get(headerName)).as("Header value not found").isEqualTo(Arrays.asList(headerValue1, headerValue2));
			assertThat(response.getHeaders().get(headerName)).as("Header value not found").isEqualTo(Arrays.asList(headerValue1, headerValue2));

			byte[] result = FileCopyUtils.copyToByteArray(response.getBody());
			assertThat(Arrays.equals(body, result)).as("Invalid body").isTrue();
			FileCopyUtils.copyToByteArray(response.getBody());
			assertThat(Arrays.equals(body, result)).as("Invalid body").isTrue();
		}
		finally {
			response.close();
		}
	}

}
