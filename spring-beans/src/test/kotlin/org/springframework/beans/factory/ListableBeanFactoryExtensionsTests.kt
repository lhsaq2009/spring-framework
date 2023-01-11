package org.springframework.beans.factory

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

/**
 * Mock object based tests for ListableBeanFactory Kotlin extensions
 *
 * @author Sebastien Deleuze
 */
class ListableBeanFactoryExtensionsTests {

	val lbf = mockk<ListableBeanFactory>(relaxed = true)

	@Test
	fun `getBeanNamesForType with reified type parameters`() {
		lbf.getBeanNamesForType<Foo>()
		verify { lbf.getBeanNamesForType(Foo::class.java, true , true) }
	}

	@Test
	fun `getBeanNamesForType with reified type parameters and Boolean`() {
		lbf.getBeanNamesForType<Foo>(false)
		verify { lbf.getBeanNamesForType(Foo::class.java, false , true) }
	}

	@Test
	fun `getBeanNamesForType with reified type parameters, Boolean and Boolean`() {
		lbf.getBeanNamesForType<Foo>(false, false)
		verify { lbf.getBeanNamesForType(Foo::class.java, false , false) }
	}

	@Test
	fun `getBeansOfType with reified type parameters`() {
		lbf.getBeansOfType<Foo>()
		verify { lbf.getBeansOfType(Foo::class.java, true , true) }
	}

	@Test
	fun `getBeansOfType with reified type parameters and Boolean`() {
		lbf.getBeansOfType<Foo>(false)
		verify { lbf.getBeansOfType(Foo::class.java, false , true) }
	}

	@Test
	fun `getBeansOfType with reified type parameters, Boolean and Boolean`() {
		lbf.getBeansOfType<Foo>(false, false)
		verify { lbf.getBeansOfType(Foo::class.java, false , false) }
	}

	@Test
	fun `getBeanNamesForAnnotation with reified type parameters`() {
		lbf.getBeanNamesForAnnotation<Bar>()
		verify { lbf.getBeanNamesForAnnotation(Bar::class.java) }
	}

	@Test
	fun `getBeansWithAnnotation with reified type parameters`() {
		lbf.getBeansWithAnnotation<Bar>()
		verify { lbf.getBeansWithAnnotation(Bar::class.java) }
	}

	@Test
	fun `findAnnotationOnBean with String and reified type parameters`() {
		val name = "bar"
		lbf.findAnnotationOnBean<Bar>(name)
		verify { lbf.findAnnotationOnBean(name, Bar::class.java) }
	}

	class Foo

	annotation class Bar
}
