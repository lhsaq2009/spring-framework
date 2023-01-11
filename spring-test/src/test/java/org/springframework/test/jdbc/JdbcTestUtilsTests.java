package org.springframework.test.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link JdbcTestUtils}.
 *
 * @author Phillip Webb
 * @since 2.5.4
 * @see JdbcTestUtilsIntegrationTests
 */
@ExtendWith(MockitoExtension.class)
class JdbcTestUtilsTests {

	@Mock
	private JdbcTemplate jdbcTemplate;


	@Test
	void deleteWithoutWhereClause() throws Exception {
		given(jdbcTemplate.update("DELETE FROM person")).willReturn(10);
		int deleted = JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "person", null);
		assertThat(deleted).isEqualTo(10);
	}

	@Test
	void deleteWithWhereClause() throws Exception {
		given(jdbcTemplate.update("DELETE FROM person WHERE name = 'Bob' and age > 25")).willReturn(10);
		int deleted = JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "person", "name = 'Bob' and age > 25");
		assertThat(deleted).isEqualTo(10);
	}

	@Test
	void deleteWithWhereClauseAndArguments() throws Exception {
		given(jdbcTemplate.update("DELETE FROM person WHERE name = ? and age > ?", "Bob", 25)).willReturn(10);
		int deleted = JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "person", "name = ? and age > ?", "Bob", 25);
		assertThat(deleted).isEqualTo(10);
	}

}
