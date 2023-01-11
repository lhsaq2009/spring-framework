package org.springframework.core.type;

import java.net.URL;

import org.junit.jupiter.api.Test;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.testfixture.EnabledForTestGroups;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.testfixture.TestGroup.LONG_RUNNING;

/**
 * Unit tests for checking the behaviour of {@link CachingMetadataReaderFactory} under
 * load. If the cache is not controlled, this test should fail with an out of memory
 * exception around entry 5k.
 *
 * @author Costin Leau
 * @author Sam Brannen
 */
@EnabledForTestGroups(LONG_RUNNING)
class CachingMetadataReaderLeakTests {

	private static final int ITEMS_TO_LOAD = 9999;

	private final MetadataReaderFactory mrf = new CachingMetadataReaderFactory();

	@Test
	void significantLoad() throws Exception {
		// the biggest public class in the JDK (>60k)
		URL url = getClass().getResource("/java/awt/Component.class");
		assertThat(url).isNotNull();

		// look at a LOT of items
		for (int i = 0; i < ITEMS_TO_LOAD; i++) {
			Resource resource = new UrlResource(url) {

				@Override
				public boolean equals(Object obj) {
					return (obj == this);
				}

				@Override
				public int hashCode() {
					return System.identityHashCode(this);
				}
			};

			MetadataReader reader = mrf.getMetadataReader(resource);
			assertThat(reader).isNotNull();
		}

		// useful for profiling to take snapshots
		// System.in.read();
	}

}
