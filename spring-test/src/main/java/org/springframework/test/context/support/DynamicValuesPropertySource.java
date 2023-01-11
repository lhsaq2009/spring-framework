package org.springframework.test.context.support;

import java.util.Map;
import java.util.function.Supplier;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.util.StringUtils;

/**
 * {@link EnumerablePropertySource} backed by a map with dynamically supplied
 * values.
 *
 * @author Phillip Webb
 * @author Sam Brannen
 * @since 5.2.5
 */
class DynamicValuesPropertySource extends EnumerablePropertySource<Map<String, Supplier<Object>>>  {

	DynamicValuesPropertySource(String name, Map<String, Supplier<Object>> valueSuppliers) {
		super(name, valueSuppliers);
	}


	@Override
	public Object getProperty(String name) {
		Supplier<Object> valueSupplier = this.source.get(name);
		return (valueSupplier != null ? valueSupplier.get() : null);
	}

	@Override
	public boolean containsProperty(String name) {
		return this.source.containsKey(name);
	}

	@Override
	public String[] getPropertyNames() {
		return StringUtils.toStringArray(this.source.keySet());
	}

}
