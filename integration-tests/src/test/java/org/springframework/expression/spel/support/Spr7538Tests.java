package org.springframework.expression.spel.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.MethodExecutor;

class Spr7538Tests {

	@Test
	void repro() throws Exception {
		AlwaysTrueReleaseStrategy target = new AlwaysTrueReleaseStrategy();
		BeanFactoryTypeConverter converter = new BeanFactoryTypeConverter();

		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setTypeConverter(converter);

		List<Foo> arguments = Collections.emptyList();

		List<TypeDescriptor> paramDescriptors = new ArrayList<>();
		Method method = AlwaysTrueReleaseStrategy.class.getMethod("checkCompleteness", List.class);
		paramDescriptors.add(new TypeDescriptor(new MethodParameter(method, 0)));


		List<TypeDescriptor> argumentTypes = new ArrayList<>();
		argumentTypes.add(TypeDescriptor.forObject(arguments));
		ReflectiveMethodResolver resolver = new ReflectiveMethodResolver();
		MethodExecutor executor = resolver.resolve(context, target, "checkCompleteness", argumentTypes);

		Object result = executor.execute(context, target, arguments);
		System.out.println("Result: " + result);
	}

	static class AlwaysTrueReleaseStrategy {
		public boolean checkCompleteness(List<Foo> messages) {
			return true;
		}
	}

	static class Foo{}
}
