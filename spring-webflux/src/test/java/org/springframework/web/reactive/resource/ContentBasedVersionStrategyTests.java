package org.springframework.web.reactive.resource;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ContentVersionStrategy}.
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 */
public class ContentBasedVersionStrategyTests {

	private ContentVersionStrategy strategy = new ContentVersionStrategy();


	@BeforeEach
	public void setup() {
		VersionResourceResolver versionResourceResolver = new VersionResourceResolver();
		versionResourceResolver.setStrategyMap(Collections.singletonMap("/**", this.strategy));
	}

	@Test
	public void extractVersion() {
		String hash = "7fbe76cdac6093784895bb4989203e5a";
		String path = "font-awesome/css/font-awesome.min-" + hash + ".css";

		assertThat(this.strategy.extractVersion(path)).isEqualTo(hash);
		assertThat(this.strategy.extractVersion("foo/bar.css")).isNull();
	}

	@Test
	public void removeVersion() {
		String hash = "7fbe76cdac6093784895bb4989203e5a";
		String path = "font-awesome/css/font-awesome.min%s%s.css";

		assertThat(this.strategy.removeVersion(String.format(path, "-", hash), hash)).isEqualTo(String.format(path, "", ""));
	}

	@Test
	public void getResourceVersion() throws Exception {
		Resource expected = new ClassPathResource("test/bar.css", getClass());
		String hash = DigestUtils.md5DigestAsHex(FileCopyUtils.copyToByteArray(expected.getInputStream()));

		assertThat(this.strategy.getResourceVersion(expected).block()).isEqualTo(hash);
	}

	@Test
	public void addVersionToUrl() {
		assertThat(this.strategy.addVersion("test/bar.css", "123")).isEqualTo("test/bar-123.css");
	}

}
