package org.springframework.test.context.support;

import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

/**
 * {@code MergedTestPropertySources} encapsulates the <em>merged</em>
 * property sources declared on a test class and all of its superclasses
 * via {@link TestPropertySource @TestPropertySource}.
 *
 * @author Sam Brannen
 * @since 4.1
 * @see TestPropertySource
 */
class MergedTestPropertySources {

	private static final MergedTestPropertySources empty = new MergedTestPropertySources(new String[0], new String[0]);

	private final String[] locations;

	private final String[] properties;


	/**
	 * Factory for an <em>empty</em> {@code MergedTestPropertySources} instance.
	 */
	static MergedTestPropertySources empty() {
		return empty;
	}


	/**
	 * Create a {@code MergedTestPropertySources} instance with the supplied
	 * {@code locations} and {@code properties}.
	 * @param locations the resource locations of properties files; may be
	 * empty but never {@code null}
	 * @param properties the properties in the form of {@code key=value} pairs;
	 * may be empty but never {@code null}
	 */
	MergedTestPropertySources(String[] locations, String[] properties) {
		Assert.notNull(locations, "The locations array must not be null");
		Assert.notNull(properties, "The properties array must not be null");
		this.locations = locations;
		this.properties = properties;
	}

	/**
	 * Get the resource locations of properties files.
	 * @see TestPropertySource#locations()
	 */
	String[] getLocations() {
		return this.locations;
	}

	/**
	 * Get the properties in the form of <em>key-value</em> pairs.
	 * @see TestPropertySource#properties()
	 */
	String[] getProperties() {
		return this.properties;
	}

}
