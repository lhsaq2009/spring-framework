package org.springframework.context.expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Stephane Nicoll
 */
public class CachedExpressionEvaluatorTests {

	private final TestExpressionEvaluator expressionEvaluator = new TestExpressionEvaluator();

	@Test
	public void parseNewExpression() {
		Method method = ReflectionUtils.findMethod(getClass(), "toString");
		Expression expression = expressionEvaluator.getTestExpression("true", method, getClass());
		hasParsedExpression("true");
		assertThat(expression.getValue()).isEqualTo(true);
		assertThat(expressionEvaluator.testCache.size()).as("Expression should be in cache").isEqualTo(1);
	}

	@Test
	public void cacheExpression() {
		Method method = ReflectionUtils.findMethod(getClass(), "toString");

		expressionEvaluator.getTestExpression("true", method, getClass());
		expressionEvaluator.getTestExpression("true", method, getClass());
		expressionEvaluator.getTestExpression("true", method, getClass());
		hasParsedExpression("true");
		assertThat(expressionEvaluator.testCache.size()).as("Only one expression should be in cache").isEqualTo(1);
	}

	@Test
	public void cacheExpressionBasedOnConcreteType() {
		Method method = ReflectionUtils.findMethod(getClass(), "toString");
		expressionEvaluator.getTestExpression("true", method, getClass());
		expressionEvaluator.getTestExpression("true", method, Object.class);
		assertThat(expressionEvaluator.testCache.size()).as("Cached expression should be based on type").isEqualTo(2);
	}

	private void hasParsedExpression(String expression) {
		verify(expressionEvaluator.getParser(), times(1)).parseExpression(expression);
	}

	private static class TestExpressionEvaluator extends CachedExpressionEvaluator {

		private final Map<ExpressionKey, Expression> testCache = new ConcurrentHashMap<>();

		public TestExpressionEvaluator() {
			super(mockSpelExpressionParser());
		}

		public Expression getTestExpression(String expression, Method method, Class<?> type) {
			return getExpression(this.testCache, new AnnotatedElementKey(method, type), expression);
		}

		private static SpelExpressionParser mockSpelExpressionParser() {
			SpelExpressionParser parser = new SpelExpressionParser();
			return spy(parser);
		}
	}

}
