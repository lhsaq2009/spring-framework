package org.springframework.test.context.jdbc;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Database configuration class for SQL script integration tests with the 'user'
 * table already created.
 *
 * @author Sam Brannen
 * @since 4.1
 */
@Configuration
public class PopulatedSchemaDatabaseConfig {

	@Bean
	PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()//
		.generateUniqueName(true)
		.addScript("classpath:/org/springframework/test/context/jdbc/schema.sql") //
		.build();
	}

	@Bean
	JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
