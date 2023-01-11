package org.springframework.jdbc.support;

import java.sql.Types;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JdbcUtils}.
 *
 * @author Thomas Risberg
 * @author Juergen Hoeller
 */
public class JdbcUtilsTests {

	@Test
	public void commonDatabaseName() {
		assertThat(JdbcUtils.commonDatabaseName("Oracle")).isEqualTo("Oracle");
		assertThat(JdbcUtils.commonDatabaseName("DB2-for-Spring")).isEqualTo("DB2");
		assertThat(JdbcUtils.commonDatabaseName("Sybase SQL Server")).isEqualTo("Sybase");
		assertThat(JdbcUtils.commonDatabaseName("Adaptive Server Enterprise")).isEqualTo("Sybase");
		assertThat(JdbcUtils.commonDatabaseName("MySQL")).isEqualTo("MySQL");
	}

	@Test
	public void resolveTypeName() {
		assertThat(JdbcUtils.resolveTypeName(Types.VARCHAR)).isEqualTo("VARCHAR");
		assertThat(JdbcUtils.resolveTypeName(Types.NUMERIC)).isEqualTo("NUMERIC");
		assertThat(JdbcUtils.resolveTypeName(Types.INTEGER)).isEqualTo("INTEGER");
		assertThat(JdbcUtils.resolveTypeName(JdbcUtils.TYPE_UNKNOWN)).isNull();
	}

	@Test
	public void convertUnderscoreNameToPropertyName() {
		assertThat(JdbcUtils.convertUnderscoreNameToPropertyName("MY_NAME")).isEqualTo("myName");
		assertThat(JdbcUtils.convertUnderscoreNameToPropertyName("yOUR_nAME")).isEqualTo("yourName");
		assertThat(JdbcUtils.convertUnderscoreNameToPropertyName("a_name")).isEqualTo("AName");
		assertThat(JdbcUtils.convertUnderscoreNameToPropertyName("someone_elses_name")).isEqualTo("someoneElsesName");
	}

}
