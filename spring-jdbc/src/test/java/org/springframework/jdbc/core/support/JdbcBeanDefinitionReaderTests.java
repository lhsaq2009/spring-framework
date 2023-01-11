package org.springframework.jdbc.core.support;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Rod Johnson
 */
public class JdbcBeanDefinitionReaderTests {

	@Test
	public void testValid() throws Exception {
		String sql = "SELECT NAME AS NAME, PROPERTY AS PROPERTY, VALUE AS VALUE FROM T";

		Connection connection = mock(Connection.class);
		DataSource dataSource = mock(DataSource.class);
		given(dataSource.getConnection()).willReturn(connection);

		ResultSet resultSet = mock(ResultSet.class);
		given(resultSet.next()).willReturn(true, true, false);
		given(resultSet.getString(1)).willReturn("one", "one");
		given(resultSet.getString(2)).willReturn("(class)", "age");
		given(resultSet.getString(3)).willReturn("org.springframework.beans.testfixture.beans.TestBean", "53");

		Statement statement = mock(Statement.class);
		given(statement.executeQuery(sql)).willReturn(resultSet);
		given(connection.createStatement()).willReturn(statement);

		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		JdbcBeanDefinitionReader reader = new JdbcBeanDefinitionReader(bf);
		reader.setDataSource(dataSource);
		reader.loadBeanDefinitions(sql);
		assertThat(bf.getBeanDefinitionCount()).as("Incorrect number of bean definitions").isEqualTo(1);
		TestBean tb = (TestBean) bf.getBean("one");
		assertThat(tb.getAge()).as("Age in TestBean was wrong.").isEqualTo(53);

		verify(resultSet).close();
		verify(statement).close();
	}

}
