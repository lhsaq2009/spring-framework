package org.springframework.context.annotation;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 */
public class Spr15275Tests {

	@Test
	public void testWithFactoryBean() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ConfigWithFactoryBean.class);
		assertThat(context.getBean(Bar.class).foo.toString()).isEqualTo("x");
		assertThat(context.getBean(Bar.class).foo).isSameAs(context.getBean(FooInterface.class));
	}

	@Test
	public void testWithAbstractFactoryBean() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ConfigWithAbstractFactoryBean.class);
		assertThat(context.getBean(Bar.class).foo.toString()).isEqualTo("x");
		assertThat(context.getBean(Bar.class).foo).isSameAs(context.getBean(FooInterface.class));
	}

	@Test
	public void testWithAbstractFactoryBeanForInterface() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ConfigWithAbstractFactoryBeanForInterface.class);
		assertThat(context.getBean(Bar.class).foo.toString()).isEqualTo("x");
		assertThat(context.getBean(Bar.class).foo).isSameAs(context.getBean(FooInterface.class));
	}

	@Test
	public void testWithAbstractFactoryBeanAsReturnType() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ConfigWithAbstractFactoryBeanAsReturnType.class);
		assertThat(context.getBean(Bar.class).foo.toString()).isEqualTo("x");
		assertThat(context.getBean(Bar.class).foo).isSameAs(context.getBean(FooInterface.class));
	}

	@Test
	public void testWithFinalFactoryBean() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ConfigWithFinalFactoryBean.class);
		assertThat(context.getBean(Bar.class).foo.toString()).isEqualTo("x");
		assertThat(context.getBean(Bar.class).foo).isSameAs(context.getBean(FooInterface.class));
	}

	@Test
	public void testWithFinalFactoryBeanAsReturnType() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ConfigWithFinalFactoryBeanAsReturnType.class);
		assertThat(context.getBean(Bar.class).foo.toString()).isEqualTo("x");
		// not same due to fallback to raw FinalFactoryBean instance with repeated getObject() invocations
		assertThat(context.getBean(Bar.class).foo).isNotSameAs(context.getBean(FooInterface.class));
	}


	@Configuration
	protected static class ConfigWithFactoryBean {

		@Bean
		public FactoryBean<Foo> foo() {
			return new FactoryBean<Foo>() {
				@Override
				public Foo getObject() {
					return new Foo("x");
				}
				@Override
				public Class<?> getObjectType() {
					return Foo.class;
				}
			};
		}

		@Bean
		public Bar bar() throws Exception {
			assertThat(foo().isSingleton()).isTrue();
			return new Bar(foo().getObject());
		}
	}


	@Configuration
	protected static class ConfigWithAbstractFactoryBean {

		@Bean
		public FactoryBean<Foo> foo() {
			return new AbstractFactoryBean<Foo>() {
				@Override
				public Foo createInstance() {
					return new Foo("x");
				}
				@Override
				public Class<?> getObjectType() {
					return Foo.class;
				}
			};
		}

		@Bean
		public Bar bar() throws Exception {
			assertThat(foo().isSingleton()).isTrue();
			return new Bar(foo().getObject());
		}
	}


	@Configuration
	protected static class ConfigWithAbstractFactoryBeanForInterface {

		@Bean
		public FactoryBean<FooInterface> foo() {
			return new AbstractFactoryBean<FooInterface>() {
				@Override
				public FooInterface createInstance() {
					return new Foo("x");
				}
				@Override
				public Class<?> getObjectType() {
					return FooInterface.class;
				}
			};
		}

		@Bean
		public Bar bar() throws Exception {
			assertThat(foo().isSingleton()).isTrue();
			return new Bar(foo().getObject());
		}
	}


	@Configuration
	protected static class ConfigWithAbstractFactoryBeanAsReturnType {

		@Bean
		public AbstractFactoryBean<FooInterface> foo() {
			return new AbstractFactoryBean<FooInterface>() {
				@Override
				public FooInterface createInstance() {
					return new Foo("x");
				}
				@Override
				public Class<?> getObjectType() {
					return Foo.class;
				}
			};
		}

		@Bean
		public Bar bar() throws Exception {
			assertThat(foo().isSingleton()).isTrue();
			return new Bar(foo().getObject());
		}
	}


	@Configuration
	protected static class ConfigWithFinalFactoryBean {

		@Bean
		public FactoryBean<FooInterface> foo() {
			return new FinalFactoryBean();
		}

		@Bean
		public Bar bar() throws Exception {
			assertThat(foo().isSingleton()).isTrue();
			return new Bar(foo().getObject());
		}
	}


	@Configuration
	protected static class ConfigWithFinalFactoryBeanAsReturnType {

		@Bean
		public FinalFactoryBean foo() {
			return new FinalFactoryBean();
		}

		@Bean
		public Bar bar() throws Exception {
			assertThat(foo().isSingleton()).isTrue();
			return new Bar(foo().getObject());
		}
	}


	private static final class FinalFactoryBean implements FactoryBean<FooInterface> {

		@Override
		public Foo getObject() {
			return new Foo("x");
		}

		@Override
		public Class<?> getObjectType() {
			return FooInterface.class;
		}
	}


	protected interface FooInterface {
	}


	protected static class Foo implements FooInterface {

		private final String value;

		public Foo(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}


	protected static class Bar {

		public final FooInterface foo;

		public Bar(FooInterface foo) {
			this.foo = foo;
		}
	}

}
