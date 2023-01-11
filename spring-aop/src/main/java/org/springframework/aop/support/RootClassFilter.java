package org.springframework.aop.support;

import java.io.Serializable;

import org.springframework.aop.ClassFilter;
import org.springframework.util.Assert;

/**
 * Simple ClassFilter implementation that passes classes (and optionally subclasses).
 *
 * @author Rod Johnson
 * @author Sam Brannen
 */
@SuppressWarnings("serial")
public class RootClassFilter implements ClassFilter, Serializable {

	private final Class<?> clazz;


	public RootClassFilter(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		this.clazz = clazz;
	}


	@Override
	public boolean matches(Class<?> candidate) {
		return this.clazz.isAssignableFrom(candidate);
	}

	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof RootClassFilter &&
				this.clazz.equals(((RootClassFilter) other).clazz)));
	}

	@Override
	public int hashCode() {
		return this.clazz.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getName() + ": " + this.clazz.getName();
	}

}
