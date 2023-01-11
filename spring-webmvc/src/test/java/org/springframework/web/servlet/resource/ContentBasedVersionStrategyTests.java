package org.springframework.web.servlet.resource;

import java.io.IOException;
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
 *
 * @author Brian Clozel
 * @author Rossen Stoyanchev
 */
public class ContentBasedVersionStrategyTests {

	private ContentVersionStrategy versionStrategy = new ContentVersionStrategy();


	@BeforeEach
	public void setup() {
		VersionResourceResolver versionResourceResolver = new VersionResourceResolver();
		versionResourceResolver.setStrategyMap(Collections.singletonMap("/**", this.versionStrategy));
	}

	@Test
	public void extractVersion() {
		String hash = "7fbe76cdac6093784895bb4989203e5a";
		String path = "font-awesome/css/font-awesome.min-" + hash + ".css";

		assertThat(this.versionStrategy.extractVersion(path)).isEqualTo(hash);
		assertThat(this.versionStrategy.extractVersion("foo/bar.css")).isNull();
	}

	@Test
	public void removeVersion() {
		String hash = "7fbe76cdac6093784895bb4989203e5a";
		String file = "font-awesome/css/font-awesome.min%s%s.css";

		assertThat(this.versionStrategy.removeVersion(String.format(file, "-", hash), hash)).isEqualTo(String.format(file, "", ""));
	}

	@Test
	public void getResourceVersion() throws IOException {
		Resource expected = new ClassPathResource("test/bar.css", getClass());
		String hash = DigestUtils.md5DigestAsHex(FileCopyUtils.copyToByteArray(expected.getInputStream()));

		assertThat(this.versionStrategy.getResourceVersion(expected)).isEqualTo(hash);
	}

	@Test
	public void addVersionToUrl() {
		assertThat(this.versionStrategy.addVersion("test/bar.css", "123")).isEqualTo("test/bar-123.css");
	}

}
