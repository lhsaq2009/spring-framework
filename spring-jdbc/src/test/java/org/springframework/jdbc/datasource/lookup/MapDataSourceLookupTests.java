package org.springframework.jdbc.datasource.lookup;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Rick Evans
 * @author Chris Beams
 */
public class MapDataSourceLookupTests {

	private static final String DATA_SOURCE_NAME = "dataSource";


	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getDataSourcesReturnsUnmodifiableMap() throws Exception {
		MapDataSourceLookup lookup = new MapDataSourceLookup();
		Map dataSources = lookup.getDataSources();

		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() ->
				dataSources.put("", ""));
	}

	@Test
	public void lookupSunnyDay() throws Exception {
		Map<String, DataSource> dataSources = new HashMap<>();
		StubDataSource expectedDataSource = new StubDataSource();
		dataSources.put(DATA_SOURCE_NAME, expectedDataSource);
		MapDataSourceLookup lookup = new MapDataSourceLookup();
		lookup.setDataSources(dataSources);
		DataSource dataSource = lookup.getDataSource(DATA_SOURCE_NAME);
		assertThat(dataSource).as("A DataSourceLookup implementation must *never* return null from getDataSource(): this one obviously (and incorrectly) is").isNotNull();
		assertThat(dataSource).isSameAs(expectedDataSource);
	}

	@Test
	public void setDataSourcesIsAnIdempotentOperation() throws Exception {
		Map<String, DataSource> dataSources = new HashMap<>();
		StubDataSource expectedDataSource = new StubDataSource();
		dataSources.put(DATA_SOURCE_NAME, expectedDataSource);
		MapDataSourceLookup lookup = new MapDataSourceLookup();
		lookup.setDataSources(dataSources);
		lookup.setDataSources(null); // must be idempotent (i.e. the following lookup must still work);
		DataSource dataSource = lookup.getDataSource(DATA_SOURCE_NAME);
		assertThat(dataSource).as("A DataSourceLookup implementation must *never* return null from getDataSource(): this one obviously (and incorrectly) is").isNotNull();
		assertThat(dataSource).isSameAs(expectedDataSource);
	}

	@Test
	public void addingDataSourcePermitsOverride() throws Exception {
		Map<String, DataSource> dataSources = new HashMap<>();
		StubDataSource overridenDataSource = new StubDataSource();
		StubDataSource expectedDataSource = new StubDataSource();
		dataSources.put(DATA_SOURCE_NAME, overridenDataSource);
		MapDataSourceLookup lookup = new MapDataSourceLookup();
		lookup.setDataSources(dataSources);
		lookup.addDataSource(DATA_SOURCE_NAME, expectedDataSource); // must override existing entry
		DataSource dataSource = lookup.getDataSource(DATA_SOURCE_NAME);
		assertThat(dataSource).as("A DataSourceLookup implementation must *never* return null from getDataSource(): this one obviously (and incorrectly) is").isNotNull();
		assertThat(dataSource).isSameAs(expectedDataSource);
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getDataSourceWhereSuppliedMapHasNonDataSourceTypeUnderSpecifiedKey() throws Exception {
		Map dataSources = new HashMap();
		dataSources.put(DATA_SOURCE_NAME, new Object());
		MapDataSourceLookup lookup = new MapDataSourceLookup(dataSources);

		assertThatExceptionOfType(ClassCastException.class).isThrownBy(() ->
				lookup.getDataSource(DATA_SOURCE_NAME));
	}

	@Test
	public void getDataSourceWhereSuppliedMapHasNoEntryForSpecifiedKey() throws Exception {
		MapDataSourceLookup lookup = new MapDataSourceLookup();

		assertThatExceptionOfType(DataSourceLookupFailureException.class).isThrownBy(() ->
				lookup.getDataSource(DATA_SOURCE_NAME));
	}

}
