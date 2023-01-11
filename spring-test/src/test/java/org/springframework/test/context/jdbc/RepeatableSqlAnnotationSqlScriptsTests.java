package org.springframework.test.context.jdbc;

import java.lang.annotation.Repeatable;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * This is a copy of {@link TransactionalSqlScriptsTests} that verifies proper
 * handling of {@link Sql @Sql} as a {@link Repeatable} annotation.
 *
 * @author Sam Brannen
 * @since 4.1
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = EmptyDatabaseConfig.class)
@Sql("schema.sql")
@Sql("data.sql")
@DirtiesContext
class RepeatableSqlAnnotationSqlScriptsTests extends AbstractTransactionalTests {

	@Test
	@Order(1)
	void classLevelScripts() {
		assertNumUsers(1);
	}

	@Test
	@Sql("drop-schema.sql")
	@Sql("schema.sql")
	@Sql("data.sql")
	@Sql("data-add-dogbert.sql")
	@Order(1)
	void methodLevelScripts() {
		assertNumUsers(2);
	}

}
