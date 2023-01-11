package org.springframework.beans.factory.wiring;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for the ClassNameBeanWiringInfoResolver class.
 *
 * @author Rick Evans
 */
public class ClassNameBeanWiringInfoResolverTests {

	@Test
	public void resolveWiringInfoWithNullBeanInstance() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ClassNameBeanWiringInfoResolver().resolveWiringInfo(null));
	}

	@Test
	public void resolveWiringInfo() {
		ClassNameBeanWiringInfoResolver resolver = new ClassNameBeanWiringInfoResolver();
		Long beanInstance = new Long(1);
		BeanWiringInfo info = resolver.resolveWiringInfo(beanInstance);
		assertThat(info).isNotNull();
		assertThat(info.getBeanName()).as("Not resolving bean name to the class name of the supplied bean instance as per class contract.").isEqualTo(beanInstance.getClass().getName());
	}

}
