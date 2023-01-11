package org.springframework.beans.factory.support;

import java.util.List;

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
public class ManagedListTests {

	@Test
	public void mergeSunnyDay() {
		ManagedList parent = new ManagedList();
		parent.add("one");
		parent.add("two");
		ManagedList child = new ManagedList();
		child.add("three");
		child.setMergeEnabled(true);
		List mergedList = child.merge(parent);
		assertThat(mergedList.size()).as("merge() obviously did not work.").isEqualTo(3);
	}

	@Test
	public void mergeWithNullParent() {
		ManagedList child = new ManagedList();
		child.add("one");
		child.setMergeEnabled(true);
		assertThat(child.merge(null)).isSameAs(child);
	}

	@Test
	public void mergeNotAllowedWhenMergeNotEnabled() {
		ManagedList child = new ManagedList();
		assertThatIllegalStateException().isThrownBy(() ->
				child.merge(null));
	}

	@Test
	public void mergeWithNonCompatibleParentType() {
		ManagedList child = new ManagedList();
		child.add("one");
		child.setMergeEnabled(true);
		assertThatIllegalArgumentException().isThrownBy(() ->
				child.merge("hello"));
	}

	@Test
	public void mergeEmptyChild() {
		ManagedList parent = new ManagedList();
		parent.add("one");
		parent.add("two");
		ManagedList child = new ManagedList();
		child.setMergeEnabled(true);
		List mergedList = child.merge(parent);
		assertThat(mergedList.size()).as("merge() obviously did not work.").isEqualTo(2);
	}

	@Test
	public void mergeChildValuesOverrideTheParents() {
		// doesn't make much sense in the context of a list...
		ManagedList parent = new ManagedList();
		parent.add("one");
		parent.add("two");
		ManagedList child = new ManagedList();
		child.add("one");
		child.setMergeEnabled(true);
		List mergedList = child.merge(parent);
		assertThat(mergedList.size()).as("merge() obviously did not work.").isEqualTo(3);
	}

}
