package org.springframework.aop.scope;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for {@link ScopedProxyUtils}.
 *
 * @author Sam Brannen
 * @since 5.1.10
 */
class ScopedProxyUtilsTests {

	@Test
	void getTargetBeanNameAndIsScopedTarget() {
		String originalBeanName = "myBean";
		String targetBeanName = ScopedProxyUtils.getTargetBeanName(originalBeanName);

		assertThat(targetBeanName).isNotEqualTo(originalBeanName).endsWith(originalBeanName);
		assertThat(ScopedProxyUtils.isScopedTarget(targetBeanName)).isTrue();
		assertThat(ScopedProxyUtils.isScopedTarget(originalBeanName)).isFalse();
	}

	@Test
	void getOriginalBeanNameAndIsScopedTarget() {
		String originalBeanName = "myBean";
		String targetBeanName = ScopedProxyUtils.getTargetBeanName(originalBeanName);
		String parsedOriginalBeanName = ScopedProxyUtils.getOriginalBeanName(targetBeanName);

		assertThat(parsedOriginalBeanName).isNotEqualTo(targetBeanName).isEqualTo(originalBeanName);
		assertThat(ScopedProxyUtils.isScopedTarget(targetBeanName)).isTrue();
		assertThat(ScopedProxyUtils.isScopedTarget(parsedOriginalBeanName)).isFalse();
	}

	@Test
	void getOriginalBeanNameForNullTargetBean() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> ScopedProxyUtils.getOriginalBeanName(null))
			.withMessage("bean name 'null' does not refer to the target of a scoped proxy");
	}

	@Test
	void getOriginalBeanNameForNonScopedTarget() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> ScopedProxyUtils.getOriginalBeanName("myBean"))
			.withMessage("bean name 'myBean' does not refer to the target of a scoped proxy");
	}

}
