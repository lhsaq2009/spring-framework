package org.springframework.jdbc.datasource.lookup;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Rick Evans
 * @author Chris Beams
 */
public class JndiDataSourceLookupTests {

	private static final String DATA_SOURCE_NAME = "Love is like a stove, burns you when it's hot";

	@Test
	public void testSunnyDay() throws Exception {
		final DataSource expectedDataSource = new StubDataSource();
		JndiDataSourceLookup lookup = new JndiDataSourceLookup() {
			@Override
			protected <T> T lookup(String jndiName, Class<T> requiredType) {
				assertThat(jndiName).isEqualTo(DATA_SOURCE_NAME);
				return requiredType.cast(expectedDataSource);
			}
		};
		DataSource dataSource = lookup.getDataSource(DATA_SOURCE_NAME);
		assertThat(dataSource).as("A DataSourceLookup implementation must *never* return null from getDataSource(): this one obviously (and incorrectly) is").isNotNull();
		assertThat(dataSource).isSameAs(expectedDataSource);
	}

	@Test
	public void testNoDataSourceAtJndiLocation() throws Exception {
		JndiDataSourceLookup lookup = new JndiDataSourceLookup() {
			@Override
			protected <T> T lookup(String jndiName, Class<T> requiredType) throws NamingException {
				assertThat(jndiName).isEqualTo(DATA_SOURCE_NAME);
				throw new NamingException();
			}
		};
		assertThatExceptionOfType(DataSourceLookupFailureException.class).isThrownBy(() ->
				lookup.getDataSource(DATA_SOURCE_NAME));
	}

}
