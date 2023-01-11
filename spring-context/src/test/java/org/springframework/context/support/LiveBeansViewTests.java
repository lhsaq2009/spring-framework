package org.springframework.context.support;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link LiveBeansView}
 *
 * @author Stephane Nicoll
 * @author Sam Brannen
 */
class LiveBeansViewTests {

	private final MockEnvironment environment = new MockEnvironment();


	@Test
	void registerIgnoredIfPropertyIsNotSet(TestInfo testInfo) throws MalformedObjectNameException {
		ConfigurableApplicationContext context = createApplicationContext("app");
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(0);
		LiveBeansView.registerApplicationContext(context);
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(0);
		LiveBeansView.unregisterApplicationContext(context);
	}

	@Test
	void registerUnregisterSingleContext(TestInfo testInfo) throws MalformedObjectNameException {
		this.environment.setProperty(LiveBeansView.MBEAN_DOMAIN_PROPERTY_NAME,
			testInfo.getTestMethod().get().getName());
		ConfigurableApplicationContext context = createApplicationContext("app");
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(0);
		LiveBeansView.registerApplicationContext(context);
		assertSingleLiveBeansViewMbean(testInfo, "app");
		LiveBeansView.unregisterApplicationContext(context);
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(0);
	}

	@Test
	void registerUnregisterSeveralContexts(TestInfo testInfo) throws MalformedObjectNameException {
		this.environment.setProperty(LiveBeansView.MBEAN_DOMAIN_PROPERTY_NAME,
			testInfo.getTestMethod().get().getName());
		ConfigurableApplicationContext context = createApplicationContext("app");
		ConfigurableApplicationContext childContext = createApplicationContext("child");
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(0);
		LiveBeansView.registerApplicationContext(context);
		assertSingleLiveBeansViewMbean(testInfo, "app");
		LiveBeansView.registerApplicationContext(childContext);
		// Only one MBean
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(1);
		LiveBeansView.unregisterApplicationContext(childContext);
		assertSingleLiveBeansViewMbean(testInfo, "app"); // Root context removes it
		LiveBeansView.unregisterApplicationContext(context);
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(0);
	}

	@Test
	void registerUnregisterSeveralContextsDifferentOrder(TestInfo testInfo) throws MalformedObjectNameException {
		this.environment.setProperty(LiveBeansView.MBEAN_DOMAIN_PROPERTY_NAME,
			testInfo.getTestMethod().get().getName());
		ConfigurableApplicationContext context = createApplicationContext("app");
		ConfigurableApplicationContext childContext = createApplicationContext("child");
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(0);
		LiveBeansView.registerApplicationContext(context);
		assertSingleLiveBeansViewMbean(testInfo, "app");
		LiveBeansView.registerApplicationContext(childContext);
		assertSingleLiveBeansViewMbean(testInfo, "app"); // Only one MBean
		LiveBeansView.unregisterApplicationContext(context);
		LiveBeansView.unregisterApplicationContext(childContext);
		assertThat(searchLiveBeansViewMeans(testInfo).size()).isEqualTo(0);
	}

	private ConfigurableApplicationContext createApplicationContext(String applicationName) {
		ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
		given(context.getEnvironment()).willReturn(this.environment);
		given(context.getApplicationName()).willReturn(applicationName);
		return context;
	}

	private void assertSingleLiveBeansViewMbean(TestInfo testInfo, String applicationName) throws MalformedObjectNameException {
		Set<ObjectName> objectNames = searchLiveBeansViewMeans(testInfo);
		assertThat(objectNames.size()).isEqualTo(1);
		assertThat(objectNames.iterator().next().getCanonicalName()).as("Wrong MBean name").isEqualTo(
			String.format("%s:application=%s", testInfo.getTestMethod().get().getName(), applicationName));
	}

	private Set<ObjectName> searchLiveBeansViewMeans(TestInfo testInfo) throws MalformedObjectNameException {
		String objectName = String.format("%s:*,%s=*", testInfo.getTestMethod().get().getName(),
			LiveBeansView.MBEAN_APPLICATION_KEY);
		return ManagementFactory.getPlatformMBeanServer().queryNames(new ObjectName(objectName), null);
	}

}
