package org.springframework.beans.testfixture.factory.xml;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public abstract class AbstractListableBeanFactoryTests extends AbstractBeanFactoryTests {

	/** Subclasses must initialize this */
	protected ListableBeanFactory getListableBeanFactory() {
		BeanFactory bf = getBeanFactory();
		if (!(bf instanceof ListableBeanFactory)) {
			throw new IllegalStateException("ListableBeanFactory required");
		}
		return (ListableBeanFactory) bf;
	}

	/**
	 * Subclasses can override this.
	 */
	@Test
	public void count() {
		assertCount(13);
	}

	protected final void assertCount(int count) {
		String[] defnames = getListableBeanFactory().getBeanDefinitionNames();
		assertThat(defnames.length == count).as("We should have " + count + " beans, not " + defnames.length).isTrue();
	}

	protected void assertTestBeanCount(int count) {
		String[] defNames = getListableBeanFactory().getBeanNamesForType(TestBean.class, true, false);
		assertThat(defNames.length == count).as("We should have " + count + " beans for class org.springframework.beans.testfixture.beans.TestBean, not " +
				defNames.length).isTrue();

		int countIncludingFactoryBeans = count + 2;
		String[] names = getListableBeanFactory().getBeanNamesForType(TestBean.class, true, true);
		assertThat(names.length == countIncludingFactoryBeans).as("We should have " + countIncludingFactoryBeans +
				" beans for class org.springframework.beans.testfixture.beans.TestBean, not " + names.length).isTrue();
	}

	@Test
	public void getDefinitionsForNoSuchClass() {
		String[] defnames = getListableBeanFactory().getBeanNamesForType(String.class);
		assertThat(defnames.length == 0).as("No string definitions").isTrue();
	}

	/**
	 * Check that count refers to factory class, not bean class. (We don't know
	 * what type factories may return, and it may even change over time.)
	 */
	@Test
	public void getCountForFactoryClass() {
		assertThat(getListableBeanFactory().getBeanNamesForType(FactoryBean.class).length == 2).as("Should have 2 factories, not " +
				getListableBeanFactory().getBeanNamesForType(FactoryBean.class).length).isTrue();

		assertThat(getListableBeanFactory().getBeanNamesForType(FactoryBean.class).length == 2).as("Should have 2 factories, not " +
				getListableBeanFactory().getBeanNamesForType(FactoryBean.class).length).isTrue();
	}

	@Test
	public void containsBeanDefinition() {
		assertThat(getListableBeanFactory().containsBeanDefinition("rod")).isTrue();
		assertThat(getListableBeanFactory().containsBeanDefinition("roderick")).isTrue();
	}

}
