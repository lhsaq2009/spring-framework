package org.springframework.beans.factory.parsing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 * @author Chris Beams
 * @since 2.0
 */
public class ParseStateTests {

	@Test
	public void testSimple() throws Exception {
		MockEntry entry = new MockEntry();

		ParseState parseState = new ParseState();
		parseState.push(entry);
		assertThat(parseState.peek()).as("Incorrect peek value.").isEqualTo(entry);
		parseState.pop();
		assertThat(parseState.peek()).as("Should get null on peek()").isNull();
	}

	@Test
	public void testNesting() throws Exception {
		MockEntry one = new MockEntry();
		MockEntry two = new MockEntry();
		MockEntry three = new MockEntry();

		ParseState parseState = new ParseState();
		parseState.push(one);
		assertThat(parseState.peek()).isEqualTo(one);
		parseState.push(two);
		assertThat(parseState.peek()).isEqualTo(two);
		parseState.push(three);
		assertThat(parseState.peek()).isEqualTo(three);

		parseState.pop();
		assertThat(parseState.peek()).isEqualTo(two);
		parseState.pop();
		assertThat(parseState.peek()).isEqualTo(one);
	}

	@Test
	public void testSnapshot() throws Exception {
		MockEntry entry = new MockEntry();

		ParseState original = new ParseState();
		original.push(entry);

		ParseState snapshot = original.snapshot();
		original.push(new MockEntry());
		assertThat(snapshot.peek()).as("Snapshot should not have been modified.").isEqualTo(entry);
	}


	private static class MockEntry implements ParseState.Entry {

	}

}
