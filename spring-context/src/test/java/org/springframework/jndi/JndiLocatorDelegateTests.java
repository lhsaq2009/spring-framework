package org.springframework.jndi;

import java.lang.reflect.Field;

import javax.naming.spi.NamingManager;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * Tests for {@link JndiLocatorDelegate}.
 *
 * @author Phillip Webb
 * @author Juergen Hoeller
 */
public class JndiLocatorDelegateTests {

	@Test
	public void isDefaultJndiEnvironmentAvailableFalse() throws Exception {
		Field builderField = NamingManager.class.getDeclaredField("initctx_factory_builder");
		builderField.setAccessible(true);
		Object oldBuilder = builderField.get(null);
		builderField.set(null, null);

		try {
			assertThat(JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable()).isEqualTo(false);
		}
		finally {
			builderField.set(null, oldBuilder);
		}
	}

}
