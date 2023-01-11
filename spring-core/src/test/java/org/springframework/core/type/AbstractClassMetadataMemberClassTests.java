package org.springframework.core.type;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Abstract base class for testing implementations of
 * {@link ClassMetadata#getMemberClassNames()}.
 *
 * @author Chris Beams
 * @since 3.1
 */
public abstract class AbstractClassMetadataMemberClassTests {

	public abstract ClassMetadata getClassMetadataFor(Class<?> clazz);

	@Test
	void withNoMemberClasses() {
		ClassMetadata metadata = getClassMetadataFor(L0_a.class);
		String[] nestedClasses = metadata.getMemberClassNames();
		assertThat(nestedClasses).isEqualTo(new String[]{});
	}

	public static class L0_a {
	}


	@Test
	void withPublicMemberClasses() {
		ClassMetadata metadata = getClassMetadataFor(L0_b.class);
		String[] nestedClasses = metadata.getMemberClassNames();
		assertThat(nestedClasses).isEqualTo(new String[]{L0_b.L1.class.getName()});
	}

	public static class L0_b {
		public static class L1 { }
	}


	@Test
	void withNonPublicMemberClasses() {
		ClassMetadata metadata = getClassMetadataFor(L0_c.class);
		String[] nestedClasses = metadata.getMemberClassNames();
		assertThat(nestedClasses).isEqualTo(new String[]{L0_c.L1.class.getName()});
	}

	public static class L0_c {
		private static class L1 { }
	}


	@Test
	void againstMemberClass() {
		ClassMetadata metadata = getClassMetadataFor(L0_b.L1.class);
		String[] nestedClasses = metadata.getMemberClassNames();
		assertThat(nestedClasses).isEqualTo(new String[]{});
	}

}
