package org.springframework.core;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 * @author Chris Shepperd
 */
@SuppressWarnings("unchecked")
class ExceptionDepthComparatorTests {

	@Test
	void targetBeforeSameDepth() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(TargetException.class, SameDepthException.class);
		assertThat(foundClass).isEqualTo(TargetException.class);
	}

	@Test
	void sameDepthBeforeTarget() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(SameDepthException.class, TargetException.class);
		assertThat(foundClass).isEqualTo(TargetException.class);
	}

	@Test
	void lowestDepthBeforeTarget() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(LowestDepthException.class, TargetException.class);
		assertThat(foundClass).isEqualTo(TargetException.class);
	}

	@Test
	void targetBeforeLowestDepth() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(TargetException.class, LowestDepthException.class);
		assertThat(foundClass).isEqualTo(TargetException.class);
	}

	@Test
	void noDepthBeforeTarget() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(NoDepthException.class, TargetException.class);
		assertThat(foundClass).isEqualTo(TargetException.class);
	}

	@Test
	void noDepthBeforeHighestDepth() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(NoDepthException.class, HighestDepthException.class);
		assertThat(foundClass).isEqualTo(HighestDepthException.class);
	}

	@Test
	void highestDepthBeforeNoDepth() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(HighestDepthException.class, NoDepthException.class);
		assertThat(foundClass).isEqualTo(HighestDepthException.class);
	}

	@Test
	void highestDepthBeforeLowestDepth() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(HighestDepthException.class, LowestDepthException.class);
		assertThat(foundClass).isEqualTo(LowestDepthException.class);
	}

	@Test
	void lowestDepthBeforeHighestDepth() throws Exception {
		Class<? extends Throwable> foundClass = findClosestMatch(LowestDepthException.class, HighestDepthException.class);
		assertThat(foundClass).isEqualTo(LowestDepthException.class);
	}

	private Class<? extends Throwable> findClosestMatch(
			Class<? extends Throwable>... classes) {
		return ExceptionDepthComparator.findClosestMatch(Arrays.asList(classes), new TargetException());
	}

	@SuppressWarnings("serial")
	public class HighestDepthException extends Throwable {
	}

	@SuppressWarnings("serial")
	public class LowestDepthException extends HighestDepthException {
	}

	@SuppressWarnings("serial")
	public class TargetException extends LowestDepthException {
	}

	@SuppressWarnings("serial")
	public class SameDepthException extends LowestDepthException {
	}

	@SuppressWarnings("serial")
	public class NoDepthException extends TargetException {
	}

}
