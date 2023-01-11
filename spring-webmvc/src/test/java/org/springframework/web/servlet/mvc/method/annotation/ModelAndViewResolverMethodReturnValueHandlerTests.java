package org.springframework.web.servlet.mvc.method.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.TestBean;
import org.springframework.core.MethodParameter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Test fixture with {@link ModelAndViewResolverMethodReturnValueHandler}.
 *
 * @author Rossen Stoyanchev
 */
public class ModelAndViewResolverMethodReturnValueHandlerTests {

	private ModelAndViewResolverMethodReturnValueHandler handler;

	private List<ModelAndViewResolver> mavResolvers;

	private ModelAndViewContainer mavContainer;

	private ServletWebRequest request;


	@BeforeEach
	public void setup() {
		mavResolvers = new ArrayList<>();
		handler = new ModelAndViewResolverMethodReturnValueHandler(mavResolvers);
		mavContainer = new ModelAndViewContainer();
		request = new ServletWebRequest(new MockHttpServletRequest());
	}


	@Test
	public void modelAndViewResolver() throws Exception {
		MethodParameter returnType = new MethodParameter(getClass().getDeclaredMethod("testBeanReturnValue"), -1);
		mavResolvers.add(new TestModelAndViewResolver(TestBean.class));
		TestBean testBean = new TestBean("name");

		handler.handleReturnValue(testBean, returnType, mavContainer, request);

		assertThat(mavContainer.getViewName()).isEqualTo("viewName");
		assertThat(mavContainer.getModel().get("modelAttrName")).isSameAs(testBean);
		assertThat(mavContainer.isRequestHandled()).isFalse();
	}

	@Test
	public void modelAndViewResolverUnresolved() throws Exception {
		MethodParameter returnType = new MethodParameter(getClass().getDeclaredMethod("intReturnValue"), -1);
		mavResolvers.add(new TestModelAndViewResolver(TestBean.class));
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() ->
				handler.handleReturnValue(99, returnType, mavContainer, request));
	}

	@Test
	public void handleNull() throws Exception {
		MethodParameter returnType = new MethodParameter(getClass().getDeclaredMethod("testBeanReturnValue"), -1);
		handler.handleReturnValue(null, returnType, mavContainer, request);

		assertThat(mavContainer.getView()).isNull();
		assertThat(mavContainer.getViewName()).isNull();
		assertThat(mavContainer.getModel().isEmpty()).isTrue();
	}

	@Test
	public void handleSimpleType() throws Exception {
		MethodParameter returnType = new MethodParameter(getClass().getDeclaredMethod("intReturnValue"), -1);
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() ->
					handler.handleReturnValue(55, returnType, mavContainer, request));
	}

	@Test
	public void handleNonSimpleType() throws Exception{
		MethodParameter returnType = new MethodParameter(getClass().getDeclaredMethod("testBeanReturnValue"), -1);
		handler.handleReturnValue(new TestBean(), returnType, mavContainer, request);

		assertThat(mavContainer.containsAttribute("testBean")).isTrue();
	}


	@SuppressWarnings("unused")
	private int intReturnValue() {
		return 0;
	}

	@SuppressWarnings("unused")
	private TestBean testBeanReturnValue() {
		return null;
	}


	private static class TestModelAndViewResolver implements ModelAndViewResolver {

		private final Class<?> returnValueType;

		public TestModelAndViewResolver(Class<?> returnValueType) {
			this.returnValueType = returnValueType;
		}

		@Override
		public ModelAndView resolveModelAndView(Method method, Class<?> handlerType, Object returnValue,
				ExtendedModelMap model, NativeWebRequest request) {

			if (returnValue != null && returnValue.getClass().equals(returnValueType)) {
				return new ModelAndView("viewName", "modelAttrName", returnValue);
			}
			else {
				return ModelAndViewResolver.UNRESOLVED;
			}
		}
	}

}
