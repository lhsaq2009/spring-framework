package org.springframework.jdbc.core.simple;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Mock object based tests for SimpleJdbcInsert.
 *
 * @author Thomas Risberg
 */
public class SimpleJdbcInsertTests {

	private Connection connection;

	private DatabaseMetaData databaseMetaData;

	private DataSource dataSource;


	@BeforeEach
	public void setUp() throws Exception {
		connection = mock(Connection.class);
		databaseMetaData = mock(DatabaseMetaData.class);
		dataSource = mock(DataSource.class);
		given(connection.getMetaData()).willReturn(databaseMetaData);
		given(dataSource.getConnection()).willReturn(connection);
	}

	@AfterEach
	public void verifyClosed() throws Exception {
		verify(connection).close();
	}


	@Test
	public void testNoSuchTable() throws Exception {
		ResultSet resultSet = mock(ResultSet.class);
		given(resultSet.next()).willReturn(false);
		given(databaseMetaData.getDatabaseProductName()).willReturn("MyDB");
		given(databaseMetaData.getDatabaseProductName()).willReturn("MyDB");
		given(databaseMetaData.getDatabaseProductVersion()).willReturn("1.0");
		given(databaseMetaData.getUserName()).willReturn("me");
		given(databaseMetaData.storesLowerCaseIdentifiers()).willReturn(true);
		given(databaseMetaData.getTables(null, null, "x", null)).willReturn(resultSet);

		SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource).withTableName("x");
		// Shouldn't succeed in inserting into table which doesn't exist
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() ->
				insert.execute(new HashMap<>()));
		verify(resultSet).close();
	}

}
