package org.springframework.web.servlet.mvc.method.annotation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.testfixture.servlet.MockHttpServletRequest
import org.springframework.web.testfixture.servlet.MockHttpServletResponse

/**
 * @author Sebastien Deleuze
 */
class ServletAnnotationControllerHandlerMethodKotlinTests : AbstractServletHandlerMethodTests() {

	@Test
	fun dataClassBinding() {
		initServletWithControllers(DataClassController::class.java)

		val request = MockHttpServletRequest("GET", "/bind")
		request.addParameter("param1", "value1")
		request.addParameter("param2", "2")
		val response = MockHttpServletResponse()
		servlet.service(request, response)
		assertThat(response.contentAsString).isEqualTo("value1-2")
	}

	@Test
	fun dataClassBindingWithOptionalParameterAndAllParameters() {
		initServletWithControllers(DataClassController::class.java)

		val request = MockHttpServletRequest("GET", "/bind-optional-parameter")
		request.addParameter("param1", "value1")
		request.addParameter("param2", "2")
		val response = MockHttpServletResponse()
		servlet.service(request, response)
		assertThat(response.contentAsString).isEqualTo("value1-2")
	}

	@Test
	fun dataClassBindingWithOptionalParameterAndOnlyMissingParameters() {
		initServletWithControllers(DataClassController::class.java)

		val request = MockHttpServletRequest("GET", "/bind-optional-parameter")
		request.addParameter("param1", "value1")
		val response = MockHttpServletResponse()
		servlet.service(request, response)
		assertThat(response.contentAsString).isEqualTo("value1-12")
	}


	data class DataClass(val param1: String, val param2: Int)

	data class DataClassWithOptionalParameter(val param1: String, val param2: Int = 12)

	@RestController
	class DataClassController {

		@RequestMapping("/bind")
		fun handle(data: DataClass) = "${data.param1}-${data.param2}"

		@RequestMapping("/bind-optional-parameter")
		fun handle(data: DataClassWithOptionalParameter) = "${data.param1}-${data.param2}"
	}

}
