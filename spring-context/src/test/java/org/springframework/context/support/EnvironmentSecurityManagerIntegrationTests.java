package org.springframework.context.support;

import java.security.AccessControlException;
import java.security.Permission;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.testfixture.env.EnvironmentTestUtils;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests integration between Environment and SecurityManagers. See SPR-9970.
 *
 * @author Chris Beams
 */
public class EnvironmentSecurityManagerIntegrationTests {

	private SecurityManager originalSecurityManager;

	private Map<String, String> env;


	@BeforeEach
	public void setUp() {
		originalSecurityManager = System.getSecurityManager();
		env = EnvironmentTestUtils.getModifiableSystemEnvironment();
		env.put(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "p1");
	}

	@AfterEach
	public void tearDown() {
		env.remove(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		System.setSecurityManager(originalSecurityManager);
	}


	@Test
	public void securityManagerDisallowsAccessToSystemEnvironmentButAllowsAccessToIndividualKeys() {
		SecurityManager securityManager = new SecurityManager() {
			@Override
			public void checkPermission(Permission perm) {
				// Disallowing access to System#getenv means that our
				// ReadOnlySystemAttributesMap will come into play.
				if ("getenv.*".equals(perm.getName())) {
					throw new AccessControlException("Accessing the system environment is disallowed");
				}
			}
		};
		System.setSecurityManager(securityManager);

		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(bf);
		reader.register(C1.class);
		assertThat(bf.containsBean("c1")).isTrue();
	}

	@Test
	public void securityManagerDisallowsAccessToSystemEnvironmentAndDisallowsAccessToIndividualKey() {
		SecurityManager securityManager = new SecurityManager() {
			@Override
			public void checkPermission(Permission perm) {
				// Disallowing access to System#getenv means that our
				// ReadOnlySystemAttributesMap will come into play.
				if ("getenv.*".equals(perm.getName())) {
					throw new AccessControlException("Accessing the system environment is disallowed");
				}
				// Disallowing access to the spring.profiles.active property means that
				// the BeanDefinitionReader won't be able to determine which profiles are
				// active. We should see an INFO-level message in the console about this
				// and as a result, any components marked with a non-default profile will
				// be ignored.
				if (("getenv." + AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME).equals(perm.getName())) {
					throw new AccessControlException(
							format("Accessing system environment variable [%s] is disallowed",
									AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME));
				}
			}
		};
		System.setSecurityManager(securityManager);

		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(bf);
		reader.register(C1.class);
		assertThat(bf.containsBean("c1")).isFalse();
	}


	@Component("c1")
	@Profile("p1")
	static class C1 {
	}

}
