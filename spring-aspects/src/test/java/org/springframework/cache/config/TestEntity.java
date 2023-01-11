package org.springframework.cache.config;

import org.springframework.util.ObjectUtils;

/**
 * Copy of the shared {@code TestEntity}: necessary
 * due to issues with Gradle test fixtures and AspectJ configuration
 * in the Gradle build.
 *
 * <p>Simple test entity for use with caching tests.
 *
 * @author Michael Plod
 */
public class TestEntity {

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof TestEntity) {
			return ObjectUtils.nullSafeEquals(this.id, ((TestEntity) obj).id);
		}
		return false;
	}
}
