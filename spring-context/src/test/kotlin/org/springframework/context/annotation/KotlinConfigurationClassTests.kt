package org.springframework.context.annotation


import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException

class KotlinConfigurationClassTests {

	@Test
	fun `Final configuration with default proxyBeanMethods value`() {
		assertThatExceptionOfType(BeanDefinitionParsingException::class.java).isThrownBy {
			AnnotationConfigApplicationContext(FinalConfigurationWithProxy::class.java)
		}
	}

	@Test
	fun `Final configuration with proxyBeanMethods set to false`() {
		val context = AnnotationConfigApplicationContext(FinalConfigurationWithoutProxy::class.java)
		val foo = context.getBean<Foo>()
		assertThat(context.getBean<Bar>().foo).isEqualTo(foo)
	}


	@Configuration
	class FinalConfigurationWithProxy {

		@Bean
		fun foo() = Foo()

		@Bean
		fun bar(foo: Foo) = Bar(foo)
	}

	@Configuration(proxyBeanMethods = false)
	class FinalConfigurationWithoutProxy {

		@Bean
		fun foo() = Foo()

		@Bean
		fun bar(foo: Foo) = Bar(foo)
	}

	class Foo

	class Bar(val foo: Foo)
}
