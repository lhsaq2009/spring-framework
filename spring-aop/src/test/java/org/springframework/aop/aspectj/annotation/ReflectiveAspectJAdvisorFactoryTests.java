package org.springframework.aop.aspectj.annotation;

/**
 * Tests for {@link ReflectiveAspectJAdvisorFactory}.
 *
 * <p>Tests are inherited: we only set the test fixture here.
 *
 * @author Rod Johnson
 * @since 2.0
 */
class ReflectiveAspectJAdvisorFactoryTests extends AbstractAspectJAdvisorFactoryTests {

	@Override
	protected AspectJAdvisorFactory getFixture() {
		return new ReflectiveAspectJAdvisorFactory();
	}

}
