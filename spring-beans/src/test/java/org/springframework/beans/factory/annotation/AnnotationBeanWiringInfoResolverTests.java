package org.springframework.beans.factory.annotation;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.wiring.BeanWiringInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Rick Evans
 * @author Chris Beams
 */
public class AnnotationBeanWiringInfoResolverTests {

	@Test
	public void testResolveWiringInfo() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new AnnotationBeanWiringInfoResolver().resolveWiringInfo(null));
	}

	@Test
	public void testResolveWiringInfoWithAnInstanceOfANonAnnotatedClass() {
		AnnotationBeanWiringInfoResolver resolver = new AnnotationBeanWiringInfoResolver();
		BeanWiringInfo info = resolver.resolveWiringInfo("java.lang.String is not @Configurable");
		assertThat(info).as("Must be returning null for a non-@Configurable class instance").isNull();
	}

	@Test
	public void testResolveWiringInfoWithAnInstanceOfAnAnnotatedClass() {
		AnnotationBeanWiringInfoResolver resolver = new AnnotationBeanWiringInfoResolver();
		BeanWiringInfo info = resolver.resolveWiringInfo(new Soap());
		assertThat(info).as("Must *not* be returning null for a non-@Configurable class instance").isNotNull();
	}

	@Test
	public void testResolveWiringInfoWithAnInstanceOfAnAnnotatedClassWithAutowiringTurnedOffExplicitly() {
		AnnotationBeanWiringInfoResolver resolver = new AnnotationBeanWiringInfoResolver();
		BeanWiringInfo info = resolver.resolveWiringInfo(new WirelessSoap());
		assertThat(info).as("Must *not* be returning null for an @Configurable class instance even when autowiring is NO").isNotNull();
		assertThat(info.indicatesAutowiring()).isFalse();
		assertThat(info.getBeanName()).isEqualTo(WirelessSoap.class.getName());
	}

	@Test
	public void testResolveWiringInfoWithAnInstanceOfAnAnnotatedClassWithAutowiringTurnedOffExplicitlyAndCustomBeanName() {
		AnnotationBeanWiringInfoResolver resolver = new AnnotationBeanWiringInfoResolver();
		BeanWiringInfo info = resolver.resolveWiringInfo(new NamedWirelessSoap());
		assertThat(info).as("Must *not* be returning null for an @Configurable class instance even when autowiring is NO").isNotNull();
		assertThat(info.indicatesAutowiring()).isFalse();
		assertThat(info.getBeanName()).isEqualTo("DerBigStick");
	}


	@Configurable()
	private static class Soap {
	}


	@Configurable(autowire = Autowire.NO)
	private static class WirelessSoap {
	}


	@Configurable(autowire = Autowire.NO, value = "DerBigStick")
	private static class NamedWirelessSoap {
	}

}
