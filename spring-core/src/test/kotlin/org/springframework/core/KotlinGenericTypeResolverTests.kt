package org.springframework.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.GenericTypeResolver.resolveReturnTypeArgument
import java.lang.reflect.Method

/**
 * Tests for Kotlin support in [GenericTypeResolver].
 *
 * @author Konrad Kaminski
 * @author Sebastien Deleuze
 */
class KotlinGenericTypeResolverTests {

	@Test
	fun methodReturnTypes() {
		assertThat(resolveReturnTypeArgument(findMethod(MyTypeWithMethods::class.java, "integer")!!,
				MyInterfaceType::class.java)).isEqualTo(Integer::class.java)
		assertThat(resolveReturnTypeArgument(findMethod(MyTypeWithMethods::class.java, "string")!!,
				MyInterfaceType::class.java)).isEqualTo(String::class.java)
		assertThat(resolveReturnTypeArgument(findMethod(MyTypeWithMethods::class.java, "raw")!!,
				MyInterfaceType::class.java)).isNull()
		assertThat(resolveReturnTypeArgument(findMethod(MyTypeWithMethods::class.java, "object")!!,
				MyInterfaceType::class.java)).isNull()
	}

	private fun findMethod(clazz: Class<*>, name: String): Method? =
			clazz.methods.firstOrNull { it.name == name }

	open class MyTypeWithMethods<T> {
		suspend fun integer(): MyInterfaceType<Int>? = null

		suspend fun string(): MySimpleInterfaceType? = null

		suspend fun `object`(): Any? = null

		suspend fun raw(): MyInterfaceType<*>? = null
	}

	interface MyInterfaceType<T>

	interface MySimpleInterfaceType: MyInterfaceType<String>

	open class MySimpleTypeWithMethods: MyTypeWithMethods<Int>()
}
