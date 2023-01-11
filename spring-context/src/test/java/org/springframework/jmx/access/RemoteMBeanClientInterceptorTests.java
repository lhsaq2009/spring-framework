package org.springframework.jmx.access;

import java.net.BindException;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.jupiter.api.AfterEach;

import org.springframework.util.SocketUtils;

/**
 * @author Rob Harrop
 * @author Chris Beams
 * @author Sam Brannen
 */
class RemoteMBeanClientInterceptorTests extends MBeanClientInterceptorTests {

	private final int servicePort = SocketUtils.findAvailableTcpPort();

	private final String serviceUrl = "service:jmx:jmxmp://localhost:" + servicePort;


	private JMXConnectorServer connectorServer;

	private JMXConnector connector;


	@Override
	public void onSetUp() throws Exception {
		super.onSetUp();
		this.connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(getServiceUrl(), null, getServer());
		try {
			this.connectorServer.start();
		}
		catch (BindException ex) {
			System.out.println("Skipping remote JMX tests because binding to local port ["
					+ this.servicePort + "] failed: " + ex.getMessage());
			runTests = false;
		}
	}

	private JMXServiceURL getServiceUrl() throws MalformedURLException {
		return new JMXServiceURL(this.serviceUrl);
	}

	@Override
	protected MBeanServerConnection getServerConnection() throws Exception {
		this.connector = JMXConnectorFactory.connect(getServiceUrl());
		return this.connector.getMBeanServerConnection();
	}

	@AfterEach
	@Override
	public void tearDown() throws Exception {
		if (this.connector != null) {
			this.connector.close();
		}
		if (this.connectorServer != null) {
			this.connectorServer.stop();
		}
		if (runTests) {
			super.tearDown();
		}
	}

}
