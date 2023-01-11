package org.springframework.aop.aspectj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public class ThisAndTargetSelectionOnlyPointcutsTests {

	private TestInterface testBean;

	private Counter thisAsClassCounter;
	private Counter thisAsInterfaceCounter;
	private Counter targetAsClassCounter;
	private Counter targetAsInterfaceCounter;
	private Counter thisAsClassAndTargetAsClassCounter;
	private Counter thisAsInterfaceAndTargetAsInterfaceCounter;
	private Counter thisAsInterfaceAndTargetAsClassCounter;


	@BeforeEach
	public void setup() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		testBean = (TestInterface) ctx.getBean("testBean");
		thisAsClassCounter = (Counter) ctx.getBean("thisAsClassCounter");
		thisAsInterfaceCounter = (Counter) ctx.getBean("thisAsInterfaceCounter");
		targetAsClassCounter = (Counter) ctx.getBean("targetAsClassCounter");
		targetAsInterfaceCounter = (Counter) ctx.getBean("targetAsInterfaceCounter");
		thisAsClassAndTargetAsClassCounter = (Counter) ctx.getBean("thisAsClassAndTargetAsClassCounter");
		thisAsInterfaceAndTargetAsInterfaceCounter = (Counter) ctx.getBean("thisAsInterfaceAndTargetAsInterfaceCounter");
		thisAsInterfaceAndTargetAsClassCounter = (Counter) ctx.getBean("thisAsInterfaceAndTargetAsClassCounter");

		thisAsClassCounter.reset();
		thisAsInterfaceCounter.reset();
		targetAsClassCounter.reset();
		targetAsInterfaceCounter.reset();
		thisAsClassAndTargetAsClassCounter.reset();
		thisAsInterfaceAndTargetAsInterfaceCounter.reset();
		thisAsInterfaceAndTargetAsClassCounter.reset();
	}


	@Test
	public void testThisAsClassDoesNotMatch() {
		testBean.doIt();
		assertThat(thisAsClassCounter.getCount()).isEqualTo(0);
	}

	@Test
	public void testThisAsInterfaceMatch() {
		testBean.doIt();
		assertThat(thisAsInterfaceCounter.getCount()).isEqualTo(1);
	}

	@Test
	public void testTargetAsClassDoesMatch() {
		testBean.doIt();
		assertThat(targetAsClassCounter.getCount()).isEqualTo(1);
	}

	@Test
	public void testTargetAsInterfaceMatch() {
		testBean.doIt();
		assertThat(targetAsInterfaceCounter.getCount()).isEqualTo(1);
	}

	@Test
	public void testThisAsClassAndTargetAsClassCounterNotMatch() {
		testBean.doIt();
		assertThat(thisAsClassAndTargetAsClassCounter.getCount()).isEqualTo(0);
	}

	@Test
	public void testThisAsInterfaceAndTargetAsInterfaceCounterMatch() {
		testBean.doIt();
		assertThat(thisAsInterfaceAndTargetAsInterfaceCounter.getCount()).isEqualTo(1);
	}

	@Test
	public void testThisAsInterfaceAndTargetAsClassCounterMatch() {
		testBean.doIt();
		assertThat(thisAsInterfaceAndTargetAsInterfaceCounter.getCount()).isEqualTo(1);
	}

}


interface TestInterface {
	public void doIt();
}


class TestImpl implements TestInterface {
	@Override
	public void doIt() {
	}
}
