package org.springframework.jdbc.support;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rod Johnson
 * @since 13-Jan-03
 */
public class SQLStateExceptionTranslatorTests {

	private static final String sql = "SELECT FOO FROM BAR";

	private final SQLStateSQLExceptionTranslator trans = new SQLStateSQLExceptionTranslator();

	// ALSO CHECK CHAIN of SQLExceptions!?
	// also allow chain of translators? default if can't do specific?

	@Test
	public void badSqlGrammar() {
		SQLException sex = new SQLException("Message", "42001", 1);
		try {
			throw this.trans.translate("task", sql, sex);
		}
		catch (BadSqlGrammarException ex) {
			// OK
			assertThat(sql.equals(ex.getSql())).as("SQL is correct").isTrue();
			assertThat(sex.equals(ex.getSQLException())).as("Exception matches").isTrue();
		}
	}

	@Test
	public void invalidSqlStateCode() {
		SQLException sex = new SQLException("Message", "NO SUCH CODE", 1);
		try {
			throw this.trans.translate("task", sql, sex);
		}
		catch (UncategorizedSQLException ex) {
			// OK
			assertThat(sql.equals(ex.getSql())).as("SQL is correct").isTrue();
			assertThat(sex.equals(ex.getSQLException())).as("Exception matches").isTrue();
		}
	}

	/**
	 * PostgreSQL can return null.
	 * SAP DB can apparently return empty SQL code.
	 * Bug 729170
	 */
	@Test
	public void malformedSqlStateCodes() {
		SQLException sex = new SQLException("Message", null, 1);
		testMalformedSqlStateCode(sex);

		sex = new SQLException("Message", "", 1);
		testMalformedSqlStateCode(sex);

		// One char's not allowed
		sex = new SQLException("Message", "I", 1);
		testMalformedSqlStateCode(sex);
	}


	private void testMalformedSqlStateCode(SQLException sex) {
		try {
			throw this.trans.translate("task", sql, sex);
		}
		catch (UncategorizedSQLException ex) {
			// OK
			assertThat(sql.equals(ex.getSql())).as("SQL is correct").isTrue();
			assertThat(sex.equals(ex.getSQLException())).as("Exception matches").isTrue();
		}
	}

}
