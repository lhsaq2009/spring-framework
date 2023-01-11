package org.springframework.test.context.jdbc.merging;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.AbstractTransactionalTests;
import org.springframework.test.context.jdbc.EmptyDatabaseConfig;
import org.springframework.test.context.jdbc.SqlMergeMode;

/**
 * Abstract base class for tests involving {@link SqlMergeMode @SqlMergeMode}.
 *
 * @author Sam Brannen
 * @since 5.2
 */
@ContextConfiguration(classes = EmptyDatabaseConfig.class)
@DirtiesContext
abstract class AbstractSqlMergeModeTests extends AbstractTransactionalTests {

}
