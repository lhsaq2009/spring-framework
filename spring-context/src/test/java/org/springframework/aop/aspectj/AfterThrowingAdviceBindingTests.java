package org.springframework.aop.aspectj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.aop.aspectj.AfterThrowingAdviceBindingTestAspect.AfterThrowingAdviceBindingCollaborator;
import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for various parameter binding scenarios with before advice.
 *
 * @author Adrian Colyer
 * @author Chris Beams
 */
public class AfterThrowingAdviceBindingTests {

	private ITestBean testBean;

	private AfterThrowingAdviceBindingTestAspect afterThrowingAdviceAspect;

	private AfterThrowingAdviceBindingCollaborator mockCollaborator;


	@BeforeEach
	public void setup() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

		testBean = (ITestBean) ctx.getBean("testBean");
		afterThrowingAdviceAspect = (AfterThrowingAdviceBindingTestAspect) ctx.getBean("testAspect");

		mockCollaborator = mock(AfterThrowingAdviceBindingCollaborator.class);
		afterThrowingAdviceAspect.setCollaborator(mockCollaborator);
	}


	@Test
	public void testSimpleAfterThrowing() throws Throwable {
		assertThatExceptionOfType(Throwable.class).isThrownBy(() ->
				this.testBean.exceptional(new Throwable()));
		verify(mockCollaborator).noArgs();
	}

	@Test
	public void testAfterThrowingWithBinding() throws Throwable {
		Throwable t = new Throwable();
		assertThatExceptionOfType(Throwable.class).isThrownBy(() ->
				this.testBean.exceptional(t));
		verify(mockCollaborator).oneThrowable(t);
	}

	@Test
	public void testAfterThrowingWithNamedTypeRestriction() throws Throwable {
		Throwable t = new Throwable();
		assertThatExceptionOfType(Throwable.class).isThrownBy(() ->
				this.testBean.exceptional(t));
		verify(mockCollaborator).noArgs();
		verify(mockCollaborator).oneThrowable(t);
		verify(mockCollaborator).noArgsOnThrowableMatch();
	}

	@Test
	public void testAfterThrowingWithRuntimeExceptionBinding() throws Throwable {
		RuntimeException ex = new RuntimeException();
		assertThatExceptionOfType(Throwable.class).isThrownBy(() ->
				this.testBean.exceptional(ex));
		verify(mockCollaborator).oneRuntimeException(ex);
	}

	@Test
	public void testAfterThrowingWithTypeSpecified() throws Throwable {
		assertThatExceptionOfType(Throwable.class).isThrownBy(() ->
					this.testBean.exceptional(new Throwable()));
		verify(mockCollaborator).noArgsOnThrowableMatch();
	}

	@Test
	public void testAfterThrowingWithRuntimeTypeSpecified() throws Throwable {
		assertThatExceptionOfType(Throwable.class).isThrownBy(() ->
				this.testBean.exceptional(new RuntimeException()));
		verify(mockCollaborator).noArgsOnRuntimeExceptionMatch();
	}

}


final class AfterThrowingAdviceBindingTestAspect {

	// collaborator interface that makes it easy to test this aspect is
	// working as expected through mocking.
	public interface AfterThrowingAdviceBindingCollaborator {
		void noArgs();
		void oneThrowable(Throwable t);
		void oneRuntimeException(RuntimeException re);
		void noArgsOnThrowableMatch();
		void noArgsOnRuntimeExceptionMatch();
	}

	protected AfterThrowingAdviceBindingCollaborator collaborator = null;

	public void setCollaborator(AfterThrowingAdviceBindingCollaborator aCollaborator) {
		this.collaborator = aCollaborator;
	}

	public void noArgs() {
		this.collaborator.noArgs();
	}

	public void oneThrowable(Throwable t) {
		this.collaborator.oneThrowable(t);
	}

	public void oneRuntimeException(RuntimeException ex) {
		this.collaborator.oneRuntimeException(ex);
	}

	public void noArgsOnThrowableMatch() {
		this.collaborator.noArgsOnThrowableMatch();
	}

	public void noArgsOnRuntimeExceptionMatch() {
		this.collaborator.noArgsOnRuntimeExceptionMatch();
	}
}
