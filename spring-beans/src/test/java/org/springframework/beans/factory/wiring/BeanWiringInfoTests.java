package org.springframework.beans.factory.wiring;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for the BeanWiringInfo class.
 *
 * @author Rick Evans
 * @author Sam Brannen
 */
public class BeanWiringInfoTests {

	@Test
	public void ctorWithNullBeanName() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new BeanWiringInfo(null));
	}

	@Test
	public void ctorWithWhitespacedBeanName() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new BeanWiringInfo("   \t"));
	}

	@Test
	public void ctorWithEmptyBeanName() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new BeanWiringInfo(""));
	}

	@Test
	public void ctorWithNegativeIllegalAutowiringValue() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new BeanWiringInfo(-1, true));
	}

	@Test
	public void ctorWithPositiveOutOfRangeAutowiringValue() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new BeanWiringInfo(123871, true));
	}

	@Test
	public void usingAutowireCtorIndicatesAutowiring() throws Exception {
		BeanWiringInfo info = new BeanWiringInfo(BeanWiringInfo.AUTOWIRE_BY_NAME, true);
		assertThat(info.indicatesAutowiring()).isTrue();
	}

	@Test
	public void usingBeanNameCtorDoesNotIndicateAutowiring() throws Exception {
		BeanWiringInfo info = new BeanWiringInfo("fooService");
		assertThat(info.indicatesAutowiring()).isFalse();
	}

	@Test
	public void noDependencyCheckValueIsPreserved() throws Exception {
		BeanWiringInfo info = new BeanWiringInfo(BeanWiringInfo.AUTOWIRE_BY_NAME, true);
		assertThat(info.getDependencyCheck()).isTrue();
	}

	@Test
	public void dependencyCheckValueIsPreserved() throws Exception {
		BeanWiringInfo info = new BeanWiringInfo(BeanWiringInfo.AUTOWIRE_BY_TYPE, false);
		assertThat(info.getDependencyCheck()).isFalse();
	}

}
