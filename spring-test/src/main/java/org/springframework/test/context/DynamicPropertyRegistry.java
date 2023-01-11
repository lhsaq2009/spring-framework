package org.springframework.test.context;

import java.util.function.Supplier;

/**
 * Registry used with {@link DynamicPropertySource @DynamicPropertySource}
 * methods so that they can add properties to the {@code Environment} that have
 * dynamically resolved values.
 *
 * @author Phillip Webb
 * @author Sam Brannen
 * @since 5.2.5
 * @see DynamicPropertySource
 */
public interface DynamicPropertyRegistry {

	/**
	 * Add a {@link Supplier} for the given property name to this registry.
	 * @param name the name of the property for which the supplier should be added
	 * @param valueSupplier a supplier that will provide the property value on demand
	 */
	void add(String name, Supplier<Object> valueSupplier);

}
