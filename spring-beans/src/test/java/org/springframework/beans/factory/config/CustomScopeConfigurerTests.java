package org.springframework.beans.factory.config;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CustomScopeConfigurer}.
 *
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class CustomScopeConfigurerTests {

	/*private static final String FOO_SCOPE = "fooScope";

	private final ConfigurableListableBeanFactory factory = new DefaultListableBeanFactory();


	@Test
	public void testWithNoScopes() {
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.postProcessBeanFactory(factory);
	}

	*//*@Test
	public void testSunnyDayWithBonaFideScopeInstance() {
		Scope scope = mock(Scope.class);
		factory.registerScope(FOO_SCOPE, scope);
		Map<String, Object> scopes = new HashMap<>();
		scopes.put(FOO_SCOPE, scope);
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
	}*//*

	@Test
	public void testSunnyDayWithBonaFideScopeClass() {
		*//*Map<String, Object> scopes = new HashMap<>();
		scopes.put(FOO_SCOPE, NoOpScope.class);
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
		boolean condition = factory.getRegisteredScope(FOO_SCOPE) instanceof NoOpScope;
		assertThat(condition).isTrue();*//*
	}

	*//*@Test
	public void testSunnyDayWithBonaFideScopeClassName() {
		Map<String, Object> scopes = new HashMap<>();
		scopes.put(FOO_SCOPE, NoOpScope.class.getName());
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
		boolean condition = factory.getRegisteredScope(FOO_SCOPE) instanceof NoOpScope;
		assertThat(condition).isTrue();
	}*//*

	@Test
	public void testWhereScopeMapHasNullScopeValueInEntrySet() {
		Map<String, Object> scopes = new HashMap<>();
		scopes.put(FOO_SCOPE, null);
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		assertThatIllegalArgumentException().isThrownBy(() ->
				figurer.postProcessBeanFactory(factory));
	}

	@Test
	public void testWhereScopeMapHasNonScopeInstanceInEntrySet() {
		Map<String, Object> scopes = new HashMap<>();
		scopes.put(FOO_SCOPE, this);  // <-- not a valid value...
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		assertThatIllegalArgumentException().isThrownBy(() ->
				figurer.postProcessBeanFactory(factory));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testWhereScopeMapHasNonStringTypedScopeNameInKeySet() {
		Map scopes = new HashMap();
		scopes.put(this, new NoOpScope());  // <-- not a valid value (the key)...
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		assertThatExceptionOfType(ClassCastException.class).isThrownBy(() ->
				figurer.postProcessBeanFactory(factory));
	}*/

}
