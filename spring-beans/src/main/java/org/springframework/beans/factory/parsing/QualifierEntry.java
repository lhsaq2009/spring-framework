package org.springframework.beans.factory.parsing;

import org.springframework.util.StringUtils;

/**
 * {@link ParseState} entry representing an autowire candidate qualifier.
 *
 * @author Mark Fisher
 * @since 2.5
 */
public class QualifierEntry implements ParseState.Entry {

	private final String typeName;


	/**
	 * Create a new {@code QualifierEntry} instance.
	 * @param typeName the name of the qualifier type
	 */
	public QualifierEntry(String typeName) {
		if (!StringUtils.hasText(typeName)) {
			throw new IllegalArgumentException("Invalid qualifier type '" + typeName + "'");
		}
		this.typeName = typeName;
	}


	@Override
	public String toString() {
		return "Qualifier '" + this.typeName + "'";
	}

}
