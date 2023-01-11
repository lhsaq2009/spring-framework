package org.springframework.aop.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.testfixture.beans.DerivedTestBean;
import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.beans.testfixture.beans.TestBean;
import org.springframework.core.testfixture.io.SerializationTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 06.04.2004
 */
public class ConcurrencyThrottleInterceptorTests {

	protected static final Log logger = LogFactory.getLog(ConcurrencyThrottleInterceptorTests.class);

	public static final int NR_OF_THREADS = 100;

	public static final int NR_OF_ITERATIONS = 1000;


	@Test
	public void testSerializable() throws Exception {
		DerivedTestBean tb = new DerivedTestBean();
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setInterfaces(ITestBean.class);
		ConcurrencyThrottleInterceptor cti = new ConcurrencyThrottleInterceptor();
		proxyFactory.addAdvice(cti);
		proxyFactory.setTarget(tb);
		ITestBean proxy = (ITestBean) proxyFactory.getProxy();
		proxy.getAge();

		ITestBean serializedProxy = (ITestBean) SerializationTestUtils.serializeAndDeserialize(proxy);
		Advised advised = (Advised) serializedProxy;
		ConcurrencyThrottleInterceptor serializedCti =
				(ConcurrencyThrottleInterceptor) advised.getAdvisors()[0].getAdvice();
		assertThat(serializedCti.getConcurrencyLimit()).isEqualTo(cti.getConcurrencyLimit());
		serializedProxy.getAge();
	}

	@Test
	public void testMultipleThreadsWithLimit1() {
		testMultipleThreads(1);
	}

	@Test
	public void testMultipleThreadsWithLimit10() {
		testMultipleThreads(10);
	}

	private void testMultipleThreads(int concurrencyLimit) {
		TestBean tb = new TestBean();
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setInterfaces(ITestBean.class);
		ConcurrencyThrottleInterceptor cti = new ConcurrencyThrottleInterceptor();
		cti.setConcurrencyLimit(concurrencyLimit);
		proxyFactory.addAdvice(cti);
		proxyFactory.setTarget(tb);
		ITestBean proxy = (ITestBean) proxyFactory.getProxy();

		Thread[] threads = new Thread[NR_OF_THREADS];
		for (int i = 0; i < NR_OF_THREADS; i++) {
			threads[i] = new ConcurrencyThread(proxy, null);
			threads[i].start();
		}
		for (int i = 0; i < NR_OF_THREADS / 10; i++) {
			try {
				Thread.sleep(5);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			threads[i] = new ConcurrencyThread(proxy,
					i % 2 == 0 ? new OutOfMemoryError() : new IllegalStateException());
			threads[i].start();
		}
		for (int i = 0; i < NR_OF_THREADS; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}


	private static class ConcurrencyThread extends Thread {

		private ITestBean proxy;
		private Throwable ex;

		public ConcurrencyThread(ITestBean proxy, Throwable ex) {
			this.proxy = proxy;
			this.ex = ex;
		}

		@Override
		public void run() {
			if (this.ex != null) {
				try {
					this.proxy.exceptional(this.ex);
				}
				catch (RuntimeException ex) {
					if (ex == this.ex) {
						logger.debug("Expected exception thrown", ex);
					}
					else {
						// should never happen
						ex.printStackTrace();
					}
				}
				catch (Error err) {
					if (err == this.ex) {
						logger.debug("Expected exception thrown", err);
					}
					else {
						// should never happen
						ex.printStackTrace();
					}
				}
				catch (Throwable ex) {
					// should never happen
					ex.printStackTrace();
				}
			}
			else {
				for (int i = 0; i < NR_OF_ITERATIONS; i++) {
					this.proxy.getName();
				}
			}
			logger.debug("finished");
		}
	}

}
