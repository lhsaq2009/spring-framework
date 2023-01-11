package org.springframework.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Test;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.support.DataAccessUtilsTests.MapPersistenceExceptionTranslator;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.stereotype.Repository;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Tests for PersistenceExceptionTranslationAdvisor's exception translation, as applied by
 * PersistenceExceptionTranslationPostProcessor.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class PersistenceExceptionTranslationAdvisorTests {

	private RuntimeException doNotTranslate = new RuntimeException();

	private PersistenceException persistenceException1 = new PersistenceException();

	protected RepositoryInterface createProxy(RepositoryInterfaceImpl target) {
		MapPersistenceExceptionTranslator mpet = new MapPersistenceExceptionTranslator();
		mpet.addTranslation(persistenceException1, new InvalidDataAccessApiUsageException("", persistenceException1));
		ProxyFactory pf = new ProxyFactory(target);
		pf.addInterface(RepositoryInterface.class);
		addPersistenceExceptionTranslation(pf, mpet);
		return (RepositoryInterface) pf.getProxy();
	}

	protected void addPersistenceExceptionTranslation(ProxyFactory pf, PersistenceExceptionTranslator pet) {
		pf.addAdvisor(new PersistenceExceptionTranslationAdvisor(pet, Repository.class));
	}

	@Test
	public void noTranslationNeeded() {
		RepositoryInterfaceImpl target = new RepositoryInterfaceImpl();
		RepositoryInterface ri = createProxy(target);

		ri.noThrowsClause();
		ri.throwsPersistenceException();

		target.setBehavior(persistenceException1);
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(
				ri::noThrowsClause)
			.isSameAs(persistenceException1);
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(
				ri::throwsPersistenceException)
			.isSameAs(persistenceException1);
	}

	@Test
	public void translationNotNeededForTheseExceptions() {
		RepositoryInterfaceImpl target = new StereotypedRepositoryInterfaceImpl();
		RepositoryInterface ri = createProxy(target);

		ri.noThrowsClause();
		ri.throwsPersistenceException();

		target.setBehavior(doNotTranslate);
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(
				ri::noThrowsClause)
			.isSameAs(doNotTranslate);
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(
				ri::throwsPersistenceException)
			.isSameAs(doNotTranslate);
	}

	@Test
	public void translationNeededForTheseExceptions() {
		doTestTranslationNeededForTheseExceptions(new StereotypedRepositoryInterfaceImpl());
	}

	@Test
	public void translationNeededForTheseExceptionsOnSuperclass() {
		doTestTranslationNeededForTheseExceptions(new MyStereotypedRepositoryInterfaceImpl());
	}

	@Test
	public void translationNeededForTheseExceptionsWithCustomStereotype() {
		doTestTranslationNeededForTheseExceptions(new CustomStereotypedRepositoryInterfaceImpl());
	}

	@Test
	public void translationNeededForTheseExceptionsOnInterface() {
		doTestTranslationNeededForTheseExceptions(new MyInterfaceStereotypedRepositoryInterfaceImpl());
	}

	@Test
	public void translationNeededForTheseExceptionsOnInheritedInterface() {
		doTestTranslationNeededForTheseExceptions(new MyInterfaceInheritedStereotypedRepositoryInterfaceImpl());
	}

	private void doTestTranslationNeededForTheseExceptions(RepositoryInterfaceImpl target) {
		RepositoryInterface ri = createProxy(target);

		target.setBehavior(persistenceException1);
		assertThatExceptionOfType(DataAccessException.class).isThrownBy(
				ri::noThrowsClause)
			.withCause(persistenceException1);

		assertThatExceptionOfType(PersistenceException.class).isThrownBy(
				ri::throwsPersistenceException)
			.isSameAs(persistenceException1);
	}


	public interface RepositoryInterface {

		void noThrowsClause();

		void throwsPersistenceException() throws PersistenceException;
	}

	public static class RepositoryInterfaceImpl implements RepositoryInterface {

		private RuntimeException runtimeException;

		public void setBehavior(RuntimeException rex) {
			this.runtimeException = rex;
		}

		@Override
		public void noThrowsClause() {
			if (runtimeException != null) {
				throw runtimeException;
			}
		}

		@Override
		public void throwsPersistenceException() throws PersistenceException {
			if (runtimeException != null) {
				throw runtimeException;
			}
		}
	}

	@Repository
	public static class StereotypedRepositoryInterfaceImpl extends RepositoryInterfaceImpl {
		// Extends above class just to add repository annotation
	}

	public static class MyStereotypedRepositoryInterfaceImpl extends StereotypedRepositoryInterfaceImpl {
	}

	@MyRepository
	public static class CustomStereotypedRepositoryInterfaceImpl extends RepositoryInterfaceImpl {
	}

	@Target({ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Repository
	public @interface MyRepository {
	}

	@Repository
	public interface StereotypedInterface {
	}

	public static class MyInterfaceStereotypedRepositoryInterfaceImpl extends RepositoryInterfaceImpl
			implements StereotypedInterface {
	}

	public interface StereotypedInheritingInterface extends StereotypedInterface {
	}

	public static class MyInterfaceInheritedStereotypedRepositoryInterfaceImpl extends RepositoryInterfaceImpl
			implements StereotypedInheritingInterface {
	}

}
