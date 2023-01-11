package org.springframework.beans.factory.support;

import java.util.Set;

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
public class ManagedSetTests {

	@Test
	public void mergeSunnyDay() {
		ManagedSet parent = new ManagedSet();
		parent.add("one");
		parent.add("two");
		ManagedSet child = new ManagedSet();
		child.add("three");
		child.setMergeEnabled(true);
		Set mergedSet = child.merge(parent);
		assertThat(mergedSet.size()).as("merge() obviously did not work.").isEqualTo(3);
	}

	@Test
	public void mergeWithNullParent() {
		ManagedSet child = new ManagedSet();
		child.add("one");
		child.setMergeEnabled(true);
		assertThat(child.merge(null)).isSameAs(child);
	}

	@Test
	public void mergeNotAllowedWhenMergeNotEnabled() {
		assertThatIllegalStateException().isThrownBy(() ->
				new ManagedSet().merge(null));
	}

	@Test
	public void mergeWithNonCompatibleParentType() {
		ManagedSet child = new ManagedSet();
		child.add("one");
		child.setMergeEnabled(true);
		assertThatIllegalArgumentException().isThrownBy(() ->
				child.merge("hello"));
	}

	@Test
	public void mergeEmptyChild() {
		ManagedSet parent = new ManagedSet();
		parent.add("one");
		parent.add("two");
		ManagedSet child = new ManagedSet();
		child.setMergeEnabled(true);
		Set mergedSet = child.merge(parent);
		assertThat(mergedSet.size()).as("merge() obviously did not work.").isEqualTo(2);
	}

	@Test
	public void mergeChildValuesOverrideTheParents() {
		// asserts that the set contract is not violated during a merge() operation...
		ManagedSet parent = new ManagedSet();
		parent.add("one");
		parent.add("two");
		ManagedSet child = new ManagedSet();
		child.add("one");
		child.setMergeEnabled(true);
		Set mergedSet = child.merge(parent);
		assertThat(mergedSet.size()).as("merge() obviously did not work.").isEqualTo(2);
	}

}
