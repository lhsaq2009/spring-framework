package org.springframework.cache.interceptor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A simple key as returned from the {@link SimpleKeyGenerator}.
 *
 * @author Phillip Webb
 * @author Juergen Hoeller
 * @since 4.0
 * @see SimpleKeyGenerator
 */
@SuppressWarnings("serial")
public class SimpleKey implements Serializable {

	/**
	 * An empty key.
	 */
	public static final SimpleKey EMPTY = new SimpleKey();


	private final Object[] params;

	// Effectively final, just re-calculated on deserialization
	private transient int hashCode;


	/**
	 * Create a new {@link SimpleKey} instance.
	 * @param elements the elements of the key
	 */
	public SimpleKey(Object... elements) {
		Assert.notNull(elements, "Elements must not be null");
		this.params = elements.clone();
		// Pre-calculate hashCode field
		this.hashCode = Arrays.deepHashCode(this.params);
	}


	@Override
	public boolean equals(@Nullable Object other) {
		return (this == other ||
				(other instanceof SimpleKey && Arrays.deepEquals(this.params, ((SimpleKey) other).params)));
	}

	@Override
	public final int hashCode() {
		// Expose pre-calculated hashCode field
		return this.hashCode;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		// Re-calculate hashCode field on deserialization
		this.hashCode = Arrays.deepHashCode(this.params);
	}

}
