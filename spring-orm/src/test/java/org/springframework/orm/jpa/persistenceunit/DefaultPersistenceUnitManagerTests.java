package org.springframework.orm.jpa.persistenceunit;

import org.junit.jupiter.api.Test;

import org.springframework.context.testfixture.index.CandidateComponentsTestClassLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.orm.jpa.domain.Person;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * Tests for {@link DefaultPersistenceUnitManager}.
 *
 * @author Stephane Nicoll
 */
public class DefaultPersistenceUnitManagerTests {

	private final DefaultPersistenceUnitManager manager = new DefaultPersistenceUnitManager();

	@Test
	public void defaultDomainWithScan() {
		this.manager.setPackagesToScan("org.springframework.orm.jpa.domain");
		this.manager.setResourceLoader(new DefaultResourceLoader(
				CandidateComponentsTestClassLoader.disableIndex(getClass().getClassLoader())));
		testDefaultDomain();
	}

	@Test
	public void defaultDomainWithIndex() {
		this.manager.setPackagesToScan("org.springframework.orm.jpa.domain");
		this.manager.setResourceLoader(new DefaultResourceLoader(
				CandidateComponentsTestClassLoader.index(getClass().getClassLoader(),
						new ClassPathResource("spring.components", Person.class))));
		testDefaultDomain();
	}

	private void testDefaultDomain() {
		SpringPersistenceUnitInfo puInfo = buildDefaultPersistenceUnitInfo();
		assertThat(puInfo.getManagedClassNames()).contains(
				"org.springframework.orm.jpa.domain.Person",
				"org.springframework.orm.jpa.domain.DriversLicense");
	}

	private SpringPersistenceUnitInfo buildDefaultPersistenceUnitInfo() {
		this.manager.preparePersistenceUnitInfos();
		return (SpringPersistenceUnitInfo) this.manager.obtainDefaultPersistenceUnitInfo();
	}

}
