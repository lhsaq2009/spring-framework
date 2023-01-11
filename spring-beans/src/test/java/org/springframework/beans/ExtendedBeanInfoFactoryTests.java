package org.springframework.beans;

import java.beans.IntrospectionException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit tests for {@link ExtendedBeanInfoTests}.
 *
 * @author Chris Beams
 */
public class ExtendedBeanInfoFactoryTests {

	private ExtendedBeanInfoFactory factory = new ExtendedBeanInfoFactory();

	@Test
	public void shouldNotSupportClassHavingOnlyVoidReturningSetter() throws IntrospectionException {
		@SuppressWarnings("unused")
		class C {
			public void setFoo(String s) { }
		}
		assertThat(factory.getBeanInfo(C.class)).isNull();
	}

	@Test
	public void shouldSupportClassHavingNonVoidReturningSetter() throws IntrospectionException {
		@SuppressWarnings("unused")
		class C {
			public C setFoo(String s) { return this; }
		}
		assertThat(factory.getBeanInfo(C.class)).isNotNull();
	}

	@Test
	public void shouldSupportClassHavingNonVoidReturningIndexedSetter() throws IntrospectionException {
		@SuppressWarnings("unused")
		class C {
			public C setFoo(int i, String s) { return this; }
		}
		assertThat(factory.getBeanInfo(C.class)).isNotNull();
	}

	@Test
	public void shouldNotSupportClassHavingNonPublicNonVoidReturningIndexedSetter() throws IntrospectionException {
		@SuppressWarnings("unused")
		class C {
			void setBar(String s) { }
		}
		assertThat(factory.getBeanInfo(C.class)).isNull();
	}

	@Test
	public void shouldNotSupportClassHavingNonVoidReturningParameterlessSetter() throws IntrospectionException {
		@SuppressWarnings("unused")
		class C {
			C setBar() { return this; }
		}
		assertThat(factory.getBeanInfo(C.class)).isNull();
	}

	@Test
	public void shouldNotSupportClassHavingNonVoidReturningMethodNamedSet() throws IntrospectionException {
		@SuppressWarnings("unused")
		class C {
			C set(String s) { return this; }
		}
		assertThat(factory.getBeanInfo(C.class)).isNull();
	}

}
