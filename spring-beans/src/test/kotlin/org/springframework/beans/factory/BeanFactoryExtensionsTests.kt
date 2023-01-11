package org.springframework.beans.factory

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.core.ResolvableType

/**
 * Mock object based tests for BeanFactory Kotlin extensions.
 *
 * @author Sebastien Deleuze
 */
class BeanFactoryExtensionsTests {

	val bf = mockk<BeanFactory>(relaxed = true)

	@Test
	fun `getBean with reified type parameters`() {
		bf.getBean<Foo>()
		verify { bf.getBean(Foo::class.java) }
	}

	@Test
	fun `getBean with String and reified type parameters`() {
		val name = "foo"
		bf.getBean<Foo>(name)
		verify { bf.getBean(name, Foo::class.java) }
	}

	@Test
	fun `getBean with reified type parameters and varargs`() {
		val arg1 = "arg1"
		val arg2 = "arg2"
		bf.getBean<Foo>(arg1, arg2)
		verify { bf.getBean(Foo::class.java, arg1, arg2) }
	}

	@Test
	fun `getBeanProvider with reified type parameters`() {
		bf.getBeanProvider<Foo>()
		verify { bf.getBeanProvider<ObjectProvider<Foo>>(ofType<ResolvableType>()) }
	}

	class Foo
}
