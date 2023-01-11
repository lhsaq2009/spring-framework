package org.springframework.jdbc.datasource.embedded;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import org.springframework.jdbc.datasource.init.DatabasePopulator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Keith Donald
 */
public class EmbeddedDatabaseFactoryTests {

	private EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();


	@Test
	public void testGetDataSource() {
		StubDatabasePopulator populator = new StubDatabasePopulator();
		factory.setDatabasePopulator(populator);
		EmbeddedDatabase db = factory.getDatabase();
		assertThat(populator.populateCalled).isTrue();
		db.shutdown();
	}


	private static class StubDatabasePopulator implements DatabasePopulator {

		private boolean populateCalled;

		@Override
		public void populate(Connection connection) {
			this.populateCalled = true;
		}
	}

}
