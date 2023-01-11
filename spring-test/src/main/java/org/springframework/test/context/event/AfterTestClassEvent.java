package org.springframework.test.context.event;

import org.springframework.test.context.TestContext;

/**
 * {@link TestContextEvent} published by the {@link EventPublishingTestExecutionListener} when
 * {@link org.springframework.test.context.TestExecutionListener#afterTestClass(TestContext)}
 * is invoked.
 *
 * @author Frank Scheffler
 * @since 5.2
 * @see org.springframework.test.context.event.annotation.AfterTestClass @AfterTestClass
 */
@SuppressWarnings("serial")
public class AfterTestClassEvent extends TestContextEvent {

	public AfterTestClassEvent(TestContext source) {
		super(source);
	}

}
