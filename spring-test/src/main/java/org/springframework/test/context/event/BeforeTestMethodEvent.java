package org.springframework.test.context.event;

import org.springframework.test.context.TestContext;

/**
 * {@link TestContextEvent} published by the {@link EventPublishingTestExecutionListener} when
 * {@link org.springframework.test.context.TestExecutionListener#beforeTestMethod(TestContext)}
 * is invoked.
 *
 * @author Frank Scheffler
 * @since 5.2
 * @see org.springframework.test.context.event.annotation.BeforeTestMethod @BeforeTestMethod
 */
@SuppressWarnings("serial")
public class BeforeTestMethodEvent extends TestContextEvent {

	public BeforeTestMethodEvent(TestContext source) {
		super(source);
	}

}
