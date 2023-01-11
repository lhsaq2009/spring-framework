package org.springframework.jdbc.datasource.lookup;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class BeanFactoryDataSourceLookupTests {

	private static final String DATASOURCE_BEAN_NAME = "dataSource";


	@Test
	public void testLookupSunnyDay() {
		BeanFactory beanFactory = mock(BeanFactory.class);

		StubDataSource expectedDataSource = new StubDataSource();
		given(beanFactory.getBean(DATASOURCE_BEAN_NAME, DataSource.class)).willReturn(expectedDataSource);

		BeanFactoryDataSourceLookup lookup = new BeanFactoryDataSourceLookup();
		lookup.setBeanFactory(beanFactory);
		DataSource dataSource = lookup.getDataSource(DATASOURCE_BEAN_NAME);
		assertThat(dataSource).as("A DataSourceLookup implementation must *never* return null from " +
				"getDataSource(): this one obviously (and incorrectly) is").isNotNull();
		assertThat(dataSource).isSameAs(expectedDataSource);
	}

	@Test
	public void testLookupWhereBeanFactoryYieldsNonDataSourceType() throws Exception {
		final BeanFactory beanFactory = mock(BeanFactory.class);

		given(beanFactory.getBean(DATASOURCE_BEAN_NAME, DataSource.class)).willThrow(
				new BeanNotOfRequiredTypeException(DATASOURCE_BEAN_NAME,
						DataSource.class, String.class));

		BeanFactoryDataSourceLookup lookup = new BeanFactoryDataSourceLookup(beanFactory);
		assertThatExceptionOfType(DataSourceLookupFailureException.class).isThrownBy(() ->
				lookup.getDataSource(DATASOURCE_BEAN_NAME));
	}

	@Test
	public void testLookupWhereBeanFactoryHasNotBeenSupplied() throws Exception {
		BeanFactoryDataSourceLookup lookup = new BeanFactoryDataSourceLookup();
		assertThatIllegalStateException().isThrownBy(() ->
				lookup.getDataSource(DATASOURCE_BEAN_NAME));
	}

}
