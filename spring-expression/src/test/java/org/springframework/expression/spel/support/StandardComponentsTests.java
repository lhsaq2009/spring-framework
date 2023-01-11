package org.springframework.expression.spel.support;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Operation;
import org.springframework.expression.OperatorOverloader;
import org.springframework.expression.TypeComparator;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypeLocator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class StandardComponentsTests {

	@Test
	public void testStandardEvaluationContext() {
		StandardEvaluationContext context = new StandardEvaluationContext();
		assertThat(context.getTypeComparator()).isNotNull();

		TypeComparator tc = new StandardTypeComparator();
		context.setTypeComparator(tc);
		assertThat(context.getTypeComparator()).isEqualTo(tc);

		TypeLocator tl = new StandardTypeLocator();
		context.setTypeLocator(tl);
		assertThat(context.getTypeLocator()).isEqualTo(tl);
	}

	@Test
	public void testStandardOperatorOverloader() throws EvaluationException {
		OperatorOverloader oo = new StandardOperatorOverloader();
		assertThat(oo.overridesOperation(Operation.ADD, null, null)).isFalse();
		assertThatExceptionOfType(EvaluationException.class).isThrownBy(() ->
				oo.operate(Operation.ADD, 2, 3));
	}

	@Test
	public void testStandardTypeLocator() {
		StandardTypeLocator tl = new StandardTypeLocator();
		List<String> prefixes = tl.getImportPrefixes();
		assertThat(prefixes.size()).isEqualTo(1);
		tl.registerImport("java.util");
		prefixes = tl.getImportPrefixes();
		assertThat(prefixes.size()).isEqualTo(2);
		tl.removeImport("java.util");
		prefixes = tl.getImportPrefixes();
		assertThat(prefixes.size()).isEqualTo(1);
	}

	@Test
	public void testStandardTypeConverter() throws EvaluationException {
		TypeConverter tc = new StandardTypeConverter();
		tc.convertValue(3, TypeDescriptor.forObject(3), TypeDescriptor.valueOf(Double.class));
	}

}
