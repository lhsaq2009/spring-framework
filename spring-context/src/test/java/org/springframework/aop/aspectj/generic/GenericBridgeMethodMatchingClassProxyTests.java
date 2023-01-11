package org.springframework.aop.aspectj.generic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for AspectJ pointcut expression matching when working with bridge methods.
 *
 * <p>This class focuses on class proxying.
 *
 * <p>See GenericBridgeMethodMatchingTests for more details.
 *
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public class GenericBridgeMethodMatchingClassProxyTests extends GenericBridgeMethodMatchingTests {

	@Test
	public void testGenericDerivedInterfaceMethodThroughClass() {
		((DerivedStringParameterizedClass) testBean).genericDerivedInterfaceMethod("");
		assertThat(counterAspect.count).isEqualTo(1);
	}

	@Test
	public void testGenericBaseInterfaceMethodThroughClass() {
		((DerivedStringParameterizedClass) testBean).genericBaseInterfaceMethod("");
		assertThat(counterAspect.count).isEqualTo(1);
	}

}
