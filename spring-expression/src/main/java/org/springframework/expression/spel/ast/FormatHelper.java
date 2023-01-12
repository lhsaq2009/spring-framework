package org.springframework.expression.spel.ast;

import java.util.List;
import java.util.StringJoiner;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * Utility methods (formatters etc) used during parsing and evaluation.
 *
 * @author Andy Clement
 */
abstract class FormatHelper {

	/**
	 * Produce a readable representation for a given method name with specified arguments.
	 * @param name the name of the method
	 * @param argumentTypes the types of the arguments to the method
	 * @return a nicely formatted representation, e.g. {@code foo(String,int)}
	 */
	public static String formatMethodForMessage(String name, List<TypeDescriptor> argumentTypes) {
		StringJoiner sj = new StringJoiner(",", "(", ")");
		for (TypeDescriptor typeDescriptor : argumentTypes) {
			if (typeDescriptor != null) {
				sj.add(formatClassNameForMessage(typeDescriptor.getType()));
			}
			else {
				sj.add(formatClassNameForMessage(null));
			}
		}
		return name + sj.toString();
	}

	/**
	 * Determine a readable name for a given Class object.
	 * <p>A String array will have the formatted name "java.lang.String[]".
	 * @param clazz the Class whose name is to be formatted
	 * @return a formatted String suitable for message inclusion
	 * @see ClassUtils#getQualifiedName(Class)
	 */
	public static String formatClassNameForMessage(@Nullable Class<?> clazz) {
		return (clazz != null ? ClassUtils.getQualifiedName(clazz) : "null");
	}

}
