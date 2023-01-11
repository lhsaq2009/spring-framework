package org.springframework.test.context.junit.jupiter.nested;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.PopulatedSchemaDatabaseConfig;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests that verify support for {@link Nested @Nested} test classes in
 * conjunction with the {@link SpringExtension}, {@link Sql @Sql}, and
 * {@link Transactional @Transactional} in a JUnit Jupiter environment.
 *
 * @author Sam Brannen
 * @since 5.1.3
 */
@SpringJUnitConfig(PopulatedSchemaDatabaseConfig.class)
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class NestedTestsWithSqlScriptsAndJUnitJupiterTests {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@BeforeTransaction
	@AfterTransaction
	void checkInitialDatabaseState() {
		assertThat(countRowsInTable("user")).isEqualTo(0);
	}

	@Test
	@Sql("/org/springframework/test/context/jdbc/data.sql")
	void sqlScripts() {
		assertThat(countRowsInTable("user")).isEqualTo(1);
	}

	private int countRowsInTable(String tableName) {
		return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
	}

	@Nested
	@SpringJUnitConfig(PopulatedSchemaDatabaseConfig.class)
	@Transactional
	class NestedTests {

		@Autowired
		JdbcTemplate jdbcTemplate;

		@BeforeTransaction
		@AfterTransaction
		void checkInitialDatabaseState() {
			assertThat(countRowsInTable("user")).isEqualTo(0);
		}

		@Test
		@Sql("/org/springframework/test/context/jdbc/data.sql")
		void nestedSqlScripts() {
			assertThat(countRowsInTable("user")).isEqualTo(1);
		}

		private int countRowsInTable(String tableName) {
			return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
		}

	}

}
