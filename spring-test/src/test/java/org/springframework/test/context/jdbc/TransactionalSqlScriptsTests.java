package org.springframework.test.context.jdbc;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Transactional integration tests for {@link Sql @Sql} support.
 *
 * @author Sam Brannen
 * @since 4.1
 */
@SpringJUnitConfig(EmptyDatabaseConfig.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@Sql({ "schema.sql", "data.sql" })
@DirtiesContext
class TransactionalSqlScriptsTests extends AbstractTransactionalTests {

	@Test
	void classLevelScripts() {
		assertNumUsers(1);
	}

	@Test
	@Sql({ "recreate-schema.sql", "data.sql", "data-add-dogbert.sql" })
	void methodLevelScripts() {
		assertNumUsers(2);
	}

}
