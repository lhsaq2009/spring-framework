package org.springframework.jdbc.datasource.init;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for {@link ResourceDatabasePopulator}.
 *
 * @author Sam Brannen
 * @since 4.1
 * @see AbstractDatabasePopulatorTests
 */
public class ResourceDatabasePopulatorTests {

	private static final Resource script1 = Mockito.mock(Resource.class);
	private static final Resource script2 = Mockito.mock(Resource.class);
	private static final Resource script3 = Mockito.mock(Resource.class);


	@Test
	public void constructWithNullResource() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ResourceDatabasePopulator((Resource) null));
	}

	@Test
	public void constructWithNullResourceArray() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ResourceDatabasePopulator((Resource[]) null));
	}

	@Test
	public void constructWithResource() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(script1);
		assertThat(databasePopulator.scripts.size()).isEqualTo(1);
	}

	@Test
	public void constructWithMultipleResources() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(script1, script2);
		assertThat(databasePopulator.scripts.size()).isEqualTo(2);
	}

	@Test
	public void constructWithMultipleResourcesAndThenAddScript() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(script1, script2);
		assertThat(databasePopulator.scripts.size()).isEqualTo(2);

		databasePopulator.addScript(script3);
		assertThat(databasePopulator.scripts.size()).isEqualTo(3);
	}

	@Test
	public void addScriptsWithNullResource() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		assertThatIllegalArgumentException().isThrownBy(() ->
				databasePopulator.addScripts((Resource) null));
	}

	@Test
	public void addScriptsWithNullResourceArray() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		assertThatIllegalArgumentException().isThrownBy(() ->
				databasePopulator.addScripts((Resource[]) null));
	}

	@Test
	public void setScriptsWithNullResource() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		assertThatIllegalArgumentException().isThrownBy(() ->
				databasePopulator.setScripts((Resource) null));
	}

	@Test
	public void setScriptsWithNullResourceArray() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		assertThatIllegalArgumentException().isThrownBy(() ->
				databasePopulator.setScripts((Resource[]) null));
	}

	@Test
	public void setScriptsAndThenAddScript() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		assertThat(databasePopulator.scripts.size()).isEqualTo(0);

		databasePopulator.setScripts(script1, script2);
		assertThat(databasePopulator.scripts.size()).isEqualTo(2);

		databasePopulator.addScript(script3);
		assertThat(databasePopulator.scripts.size()).isEqualTo(3);
	}

}
