package org.springframework.aop.aspectj.autoproxy.spr3064;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Adrian Colyer
 * @author Chris Beams
 */
public class SPR3064Tests {

	private Service service;


	@Test
	public void testServiceIsAdvised() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

		service = (Service) ctx.getBean("service");
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(
				this.service::serveMe)
			.withMessageContaining("advice invoked");
	}

}


@Retention(RetentionPolicy.RUNTIME)
@interface Transaction {
}


@Aspect
class TransactionInterceptor {

	@Around(value="execution(* *..Service.*(..)) && @annotation(transaction)")
	public Object around(ProceedingJoinPoint pjp, Transaction transaction) throws Throwable {
		throw new RuntimeException("advice invoked");
		//return pjp.proceed();
	}
}


interface Service {

	void serveMe();
}


class ServiceImpl implements Service {

	@Override
	@Transaction
	public void serveMe() {
	}
}
