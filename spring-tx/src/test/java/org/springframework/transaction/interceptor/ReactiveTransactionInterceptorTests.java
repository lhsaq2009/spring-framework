package org.springframework.transaction.interceptor;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.transaction.ReactiveTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link TransactionInterceptor} with reactive methods.
 *
 * @author Mark Paluch
 */
public class ReactiveTransactionInterceptorTests extends AbstractReactiveTransactionAspectTests {

	@Override
	protected Object advised(Object target, ReactiveTransactionManager ptm, TransactionAttributeSource[] tas) {
		TransactionInterceptor ti = new TransactionInterceptor();
		ti.setTransactionManager(ptm);
		ti.setTransactionAttributeSources(tas);

		ProxyFactory pf = new ProxyFactory(target);
		pf.addAdvice(0, ti);
		return pf.getProxy();
	}

	/**
	 * Template method to create an advised object given the
	 * target object and transaction setup.
	 * Creates a TransactionInterceptor and applies it.
	 */
	@Override
	protected Object advised(Object target, ReactiveTransactionManager ptm, TransactionAttributeSource tas) {
		TransactionInterceptor ti = new TransactionInterceptor();
		ti.setTransactionManager(ptm);

		assertThat(ti.getTransactionManager()).isEqualTo(ptm);
		ti.setTransactionAttributeSource(tas);
		assertThat(ti.getTransactionAttributeSource()).isEqualTo(tas);

		ProxyFactory pf = new ProxyFactory(target);
		pf.addAdvice(0, ti);
		return pf.getProxy();
	}

}
