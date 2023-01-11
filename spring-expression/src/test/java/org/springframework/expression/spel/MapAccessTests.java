package org.springframework.expression.spel;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.core.testfixture.EnabledForTestGroups;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.testfixture.TestGroup.PERFORMANCE;

/**
 * Testing variations on map access.
 *
 * @author Andy Clement
 */
public class MapAccessTests extends AbstractExpressionTests {

	@Test
	public void testSimpleMapAccess01() {
		evaluate("testMap.get('monday')", "montag", String.class);
	}

	@Test
	public void testMapAccessThroughIndexer() {
		evaluate("testMap['monday']", "montag", String.class);
	}

	@Test
	public void testCustomMapAccessor() throws Exception {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext ctx = TestScenarioCreator.getTestEvaluationContext();
		ctx.addPropertyAccessor(new MapAccessor());

		Expression expr = parser.parseExpression("testMap.monday");
		Object value = expr.getValue(ctx, String.class);
		assertThat(value).isEqualTo("montag");
	}

	@Test
	public void testVariableMapAccess() throws Exception {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext ctx = TestScenarioCreator.getTestEvaluationContext();
		ctx.setVariable("day", "saturday");

		Expression expr = parser.parseExpression("testMap[#day]");
		Object value = expr.getValue(ctx, String.class);
		assertThat(value).isEqualTo("samstag");
	}

	@Test
	public void testGetValue(){
		Map<String,String> props1 = new HashMap<>();
		props1.put("key1", "value1");
		props1.put("key2", "value2");
		props1.put("key3", "value3");

		Object bean = new TestBean("name1", new TestBean("name2", null, "Description 2", 15, props1), "description 1", 6, props1);

		ExpressionParser parser = new SpelExpressionParser();
		Expression expr = parser.parseExpression("testBean.properties['key2']");
		assertThat(expr.getValue(bean)).isEqualTo("value2");
	}

	@Test
	public void testGetValueFromRootMap() {
		Map<String, String> map = new HashMap<>();
		map.put("key", "value");

		ExpressionParser spelExpressionParser = new SpelExpressionParser();
		Expression expr = spelExpressionParser.parseExpression("#root['key']");
		assertThat(expr.getValue(map)).isEqualTo("value");
	}

	@Test
	@EnabledForTestGroups(PERFORMANCE)
	public void testGetValuePerformance() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("key", "value");
		EvaluationContext context = new StandardEvaluationContext(map);

		ExpressionParser spelExpressionParser = new SpelExpressionParser();
		Expression expr = spelExpressionParser.parseExpression("#root['key']");

		StopWatch s = new StopWatch();
		s.start();
		for (int i = 0; i < 10000; i++) {
			expr.getValue(context);
		}
		s.stop();
		assertThat(s.getTotalTimeMillis()).isLessThan(200L);
	}


	public static class TestBean {

		private String name;
		private TestBean testBean;
		private String description;
		private Integer priority;
		private Map<String, String> properties;

		public TestBean(String name, TestBean testBean, String description, Integer priority, Map<String, String> props) {
			this.name = name;
			this.testBean = testBean;
			this.description = description;
			this.priority = priority;
			this.properties = props;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public TestBean getTestBean() {
			return testBean;
		}

		public void setTestBean(TestBean testBean) {
			this.testBean = testBean;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Integer getPriority() {
			return priority;
		}

		public void setPriority(Integer priority) {
			this.priority = priority;
		}

		public Map<String,String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String,String> properties) {
			this.properties = properties;
		}
	}


	public static class MapAccessor implements PropertyAccessor {

		@Override
		public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
			return (((Map<?, ?>) target).containsKey(name));
		}

		@Override
		public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
			return new TypedValue(((Map<? ,?>) target).get(name));
		}

		@Override
		public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
			return true;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
			((Map<Object,Object>) target).put(name, newValue);
		}

		@Override
		public Class<?>[] getSpecificTargetClasses() {
			return new Class<?>[] {Map.class};
		}
	}

}
