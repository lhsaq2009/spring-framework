package org.springframework.web.servlet.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for {@link FixedVersionStrategy}.
 *
 * @author Brian Clozel
 * @author Rossen Stoyanchev
 */
public class FixedVersionStrategyTests {

	private static final String VERSION = "1df341f";

	private static final String PATH = "js/foo.js";


	private FixedVersionStrategy strategy;


	@BeforeEach
	public void setup() {
		this.strategy = new FixedVersionStrategy(VERSION);
	}


	@Test
	public void emptyPrefixVersion() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new FixedVersionStrategy("  "));
	}

	@Test
	public void extractVersion() {
		assertThat(this.strategy.extractVersion(VERSION + "/" + PATH)).isEqualTo(VERSION);
		assertThat(this.strategy.extractVersion(PATH)).isNull();
	}

	@Test
	public void removeVersion() {
		assertThat(this.strategy.removeVersion(VERSION + "/" + PATH, VERSION)).isEqualTo(("/" + PATH));
	}

	@Test
	public void addVersion() {
		assertThat(this.strategy.addVersion("/" + PATH, VERSION)).isEqualTo((VERSION + "/" + PATH));
	}

	@Test  // SPR-13727
	public void addVersionRelativePath() {
		String relativePath = "../" + PATH;
		assertThat(this.strategy.addVersion(relativePath, VERSION)).isEqualTo(relativePath);
	}

}
