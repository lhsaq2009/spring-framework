package org.springframework.http.client.support;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Arjen Poutsma
 */
public class ProxyFactoryBeanTests {

	ProxyFactoryBean factoryBean;

	@BeforeEach
	public void setUp() {
		factoryBean = new ProxyFactoryBean();
	}

	@Test
	public void noType() {
		factoryBean.setType(null);
		assertThatIllegalArgumentException().isThrownBy(
				factoryBean::afterPropertiesSet);
	}

	@Test
	public void noHostname() {
		factoryBean.setHostname("");
		assertThatIllegalArgumentException().isThrownBy(
				factoryBean::afterPropertiesSet);
	}

	@Test
	public void noPort() {
		factoryBean.setHostname("example.com");
		assertThatIllegalArgumentException().isThrownBy(
				factoryBean::afterPropertiesSet);
	}

	@Test
	public void normal() {
		Proxy.Type type = Proxy.Type.HTTP;
		factoryBean.setType(type);
		String hostname = "example.com";
		factoryBean.setHostname(hostname);
		int port = 8080;
		factoryBean.setPort(port);
		factoryBean.afterPropertiesSet();

		Proxy result = factoryBean.getObject();

		assertThat(result.type()).isEqualTo(type);
		InetSocketAddress address = (InetSocketAddress) result.address();
		assertThat(address.getHostName()).isEqualTo(hostname);
		assertThat(address.getPort()).isEqualTo(port);
	}

}
