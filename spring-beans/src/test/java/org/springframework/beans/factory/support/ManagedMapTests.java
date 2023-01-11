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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ManagedMapTests {

	@Test
	public void mergeSunnyDay() {
		ManagedMap parent = new ManagedMap();
		parent.put("one", "one");
		parent.put("two", "two");
		ManagedMap child = new ManagedMap();
		child.put("three", "three");
		child.setMergeEnabled(true);
		Map mergedMap = (Map) child.merge(parent);
		assertThat(mergedMap.size()).as("merge() obviously did not work.").isEqualTo(3);
	}

	@Test
	public void mergeWithNullParent() {
		ManagedMap child = new ManagedMap();
		child.setMergeEnabled(true);
		assertThat(child.merge(null)).isSameAs(child);
	}

	@Test
	public void mergeWithNonCompatibleParentType() {
		ManagedMap map = new ManagedMap();
		map.setMergeEnabled(true);
		assertThatIllegalArgumentException().isThrownBy(() ->
				map.merge("hello"));
	}

	@Test
	public void mergeNotAllowedWhenMergeNotEnabled() {
		assertThatIllegalStateException().isThrownBy(() ->
				new ManagedMap().merge(null));
	}

	@Test
	public void mergeEmptyChild() {
		ManagedMap parent = new ManagedMap();
		parent.put("one", "one");
		parent.put("two", "two");
		ManagedMap child = new ManagedMap();
		child.setMergeEnabled(true);
		Map mergedMap = (Map) child.merge(parent);
		assertThat(mergedMap.size()).as("merge() obviously did not work.").isEqualTo(2);
	}

	@Test
	public void mergeChildValuesOverrideTheParents() {
		ManagedMap parent = new ManagedMap();
		parent.put("one", "one");
		parent.put("two", "two");
		ManagedMap child = new ManagedMap();
		child.put("one", "fork");
		child.setMergeEnabled(true);
		Map mergedMap = (Map) child.merge(parent);
		// child value for 'one' must override parent value...
		assertThat(mergedMap.size()).as("merge() obviously did not work.").isEqualTo(2);
		assertThat(mergedMap.get("one")).as("Parent value not being overridden during merge().").isEqualTo("fork");
	}

}
