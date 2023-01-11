package org.springframework.beans.factory.support;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@SuppressWarnings("rawtypes")
public class ManagedPropertiesTests {

	@Test
	public void mergeSunnyDay() {
		ManagedProperties parent = new ManagedProperties();
		parent.setProperty("one", "one");
		parent.setProperty("two", "two");
		ManagedProperties child = new ManagedProperties();
		child.setProperty("three", "three");
		child.setMergeEnabled(true);
		Map mergedMap = (Map) child.merge(parent);
		assertThat(mergedMap.size()).as("merge() obviously did not work.").isEqualTo(3);
	}

	@Test
	public void mergeWithNullParent() {
		ManagedProperties child = new ManagedProperties();
		child.setMergeEnabled(true);
		assertThat(child.merge(null)).isSameAs(child);
	}

	@Test
	public void mergeWithNonCompatibleParentType() {
		ManagedProperties map = new ManagedProperties();
		map.setMergeEnabled(true);
		assertThatIllegalArgumentException().isThrownBy(() ->
				map.merge("hello"));
	}

	@Test
	public void mergeNotAllowedWhenMergeNotEnabled() {
		ManagedProperties map = new ManagedProperties();
		assertThatIllegalStateException().isThrownBy(() ->
				map.merge(null));
	}

	@Test
	public void mergeEmptyChild() {
		ManagedProperties parent = new ManagedProperties();
		parent.setProperty("one", "one");
		parent.setProperty("two", "two");
		ManagedProperties child = new ManagedProperties();
		child.setMergeEnabled(true);
		Map mergedMap = (Map) child.merge(parent);
		assertThat(mergedMap.size()).as("merge() obviously did not work.").isEqualTo(2);
	}

	@Test
	public void mergeChildValuesOverrideTheParents() {
		ManagedProperties parent = new ManagedProperties();
		parent.setProperty("one", "one");
		parent.setProperty("two", "two");
		ManagedProperties child = new ManagedProperties();
		child.setProperty("one", "fork");
		child.setMergeEnabled(true);
		Map mergedMap = (Map) child.merge(parent);
		// child value for 'one' must override parent value...
		assertThat(mergedMap.size()).as("merge() obviously did not work.").isEqualTo(2);
		assertThat(mergedMap.get("one")).as("Parent value not being overridden during merge().").isEqualTo("fork");
	}

}
