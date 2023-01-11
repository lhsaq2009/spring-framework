package org.springframework.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KotlinDefaultParameterNameDiscovererTests {

	private val parameterNameDiscoverer = DefaultParameterNameDiscoverer()

	enum class MyEnum {
		ONE, TWO
	}

	@Test  // SPR-16931
	fun getParameterNamesOnEnum() {
		val constructor = MyEnum::class.java.declaredConstructors[0]
		val actualParams = parameterNameDiscoverer.getParameterNames(constructor)
		assertThat(actualParams!!.size).isEqualTo(2)
	}
}
