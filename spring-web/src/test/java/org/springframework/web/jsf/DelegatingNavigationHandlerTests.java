package org.springframework.web.jsf;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.lang.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 */
public class DelegatingNavigationHandlerTests {

	private final MockFacesContext facesContext = new MockFacesContext();

	private final StaticListableBeanFactory beanFactory = new StaticListableBeanFactory();

	private final TestNavigationHandler origNavHandler = new TestNavigationHandler();

	private final DelegatingNavigationHandlerProxy delNavHandler = new DelegatingNavigationHandlerProxy(origNavHandler) {
		@Override
		protected BeanFactory getBeanFactory(FacesContext facesContext) {
			return beanFactory;
		}
	};


	@Test
	public void handleNavigationWithoutDecoration() {
		TestNavigationHandler targetHandler = new TestNavigationHandler();
		beanFactory.addBean("jsfNavigationHandler", targetHandler);

		delNavHandler.handleNavigation(facesContext, "fromAction", "myViewId");
		assertThat(targetHandler.lastFromAction).isEqualTo("fromAction");
		assertThat(targetHandler.lastOutcome).isEqualTo("myViewId");
	}

	@Test
	public void handleNavigationWithDecoration() {
		TestDecoratingNavigationHandler targetHandler = new TestDecoratingNavigationHandler();
		beanFactory.addBean("jsfNavigationHandler", targetHandler);

		delNavHandler.handleNavigation(facesContext, "fromAction", "myViewId");
		assertThat(targetHandler.lastFromAction).isEqualTo("fromAction");
		assertThat(targetHandler.lastOutcome).isEqualTo("myViewId");

		// Original handler must have been invoked as well...
		assertThat(origNavHandler.lastFromAction).isEqualTo("fromAction");
		assertThat(origNavHandler.lastOutcome).isEqualTo("myViewId");
	}


	static class TestNavigationHandler extends NavigationHandler {

		private String lastFromAction;
		private String lastOutcome;

		@Override
		public void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {
			lastFromAction = fromAction;
			lastOutcome = outcome;
		}
	}


	static class TestDecoratingNavigationHandler extends DecoratingNavigationHandler {

		private String lastFromAction;
		private String lastOutcome;

		@Override
		public void handleNavigation(FacesContext facesContext, @Nullable String fromAction,
				@Nullable String outcome, @Nullable NavigationHandler originalNavigationHandler) {

			lastFromAction = fromAction;
			lastOutcome = outcome;
			if (originalNavigationHandler != null) {
				originalNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
			}
		}
	}

}
