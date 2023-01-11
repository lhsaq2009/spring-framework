package org.springframework.core.env

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

/**
 * Mock object based tests for PropertyResolver Kotlin extensions.
 *
 * @author Sebastien Deleuze
 */
class KotlinPropertyResolverExtensionsTests {

	val propertyResolver = mockk<PropertyResolver>()

	@Test
	fun `get operator`() {
		every { propertyResolver.getProperty("name") } returns "foo"
		propertyResolver["name"]
		verify { propertyResolver.getProperty("name") }
	}

	@Test
	fun `getProperty extension`() {
		every { propertyResolver.getProperty("name", String::class.java) } returns "foo"
		propertyResolver.getProperty<String>("name")
		verify { propertyResolver.getProperty("name", String::class.java) }
	}

	@Test
	fun `getRequiredProperty extension`() {
		every { propertyResolver.getRequiredProperty("name", String::class.java) } returns "foo"
		propertyResolver.getRequiredProperty<String>("name")
		verify { propertyResolver.getRequiredProperty("name", String::class.java) }
	}

}
