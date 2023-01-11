package org.springframework.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Juergen Hoeller
 * @since 28.05.2004
 */
public class UserCredentialsDataSourceAdapterTests {

	@Test
	public void testStaticCredentials() throws SQLException {
		DataSource dataSource = mock(DataSource.class);
		Connection connection = mock(Connection.class);
		given(dataSource.getConnection("user", "pw")).willReturn(connection);

		UserCredentialsDataSourceAdapter adapter = new UserCredentialsDataSourceAdapter();
		adapter.setTargetDataSource(dataSource);
		adapter.setUsername("user");
		adapter.setPassword("pw");
		assertThat(adapter.getConnection()).isEqualTo(connection);
	}

	@Test
	public void testNoCredentials() throws SQLException {
		DataSource dataSource = mock(DataSource.class);
		Connection connection = mock(Connection.class);
		given(dataSource.getConnection()).willReturn(connection);
		UserCredentialsDataSourceAdapter adapter = new UserCredentialsDataSourceAdapter();
		adapter.setTargetDataSource(dataSource);
		assertThat(adapter.getConnection()).isEqualTo(connection);
	}

	@Test
	public void testThreadBoundCredentials() throws SQLException {
		DataSource dataSource = mock(DataSource.class);
		Connection connection = mock(Connection.class);
		given(dataSource.getConnection("user", "pw")).willReturn(connection);

		UserCredentialsDataSourceAdapter adapter = new UserCredentialsDataSourceAdapter();
		adapter.setTargetDataSource(dataSource);

		adapter.setCredentialsForCurrentThread("user", "pw");
		try {
			assertThat(adapter.getConnection()).isEqualTo(connection);
		}
		finally {
			adapter.removeCredentialsFromCurrentThread();
		}
	}

}
