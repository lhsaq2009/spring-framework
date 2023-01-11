package org.springframework.orm.jpa.eclipselink;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.junit.jupiter.api.Test;

import org.springframework.orm.jpa.AbstractContainerEntityManagerFactoryIntegrationTests;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EclipseLink-specific JPA tests.
 *
 * @author Juergen Hoeller
 */
public class EclipseLinkEntityManagerFactoryIntegrationTests extends AbstractContainerEntityManagerFactoryIntegrationTests {

	@Test
	public void testCanCastNativeEntityManagerFactoryToEclipseLinkEntityManagerFactoryImpl() {
		EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
		assertThat(emfi.getNativeEntityManagerFactory().getClass().getName().endsWith("EntityManagerFactoryImpl")).isTrue();
	}

	@Test
	public void testCanCastSharedEntityManagerProxyToEclipseLinkEntityManager() {
		boolean condition = sharedEntityManager instanceof JpaEntityManager;
		assertThat(condition).isTrue();
		JpaEntityManager eclipselinkEntityManager = (JpaEntityManager) sharedEntityManager;
		assertThat(eclipselinkEntityManager.getActiveSession()).isNotNull();
	}

}
