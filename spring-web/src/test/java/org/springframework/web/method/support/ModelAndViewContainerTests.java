package org.springframework.web.method.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.ui.ModelMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test fixture for {@link ModelAndViewContainer}.
 *
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class ModelAndViewContainerTests {

	private ModelAndViewContainer mavContainer;


	@BeforeEach
	public void setup() {
		this.mavContainer = new ModelAndViewContainer();
	}


	@Test
	public void getModel() {
		this.mavContainer.addAttribute("name", "value");
		assertThat(this.mavContainer.getModel().size()).isEqualTo(1);
		assertThat(this.mavContainer.getModel().get("name")).isEqualTo("value");
	}

	@Test
	public void redirectScenarioWithRedirectModel() {
		this.mavContainer.addAttribute("name1", "value1");
		this.mavContainer.setRedirectModel(new ModelMap("name2", "value2"));
		this.mavContainer.setRedirectModelScenario(true);

		assertThat(this.mavContainer.getModel().size()).isEqualTo(1);
		assertThat(this.mavContainer.getModel().get("name2")).isEqualTo("value2");
	}

	@Test
	public void redirectScenarioWithoutRedirectModel() {
		this.mavContainer.addAttribute("name", "value");
		this.mavContainer.setRedirectModelScenario(true);

		assertThat(this.mavContainer.getModel().size()).isEqualTo(1);
		assertThat(this.mavContainer.getModel().get("name")).isEqualTo("value");
	}

	@Test
	public void ignoreDefaultModel() {
		this.mavContainer.setIgnoreDefaultModelOnRedirect(true);
		this.mavContainer.addAttribute("name", "value");
		this.mavContainer.setRedirectModelScenario(true);

		assertThat(this.mavContainer.getModel().isEmpty()).isTrue();
	}

	@Test  // SPR-14045
	public void ignoreDefaultModelAndWithoutRedirectModel() {
		this.mavContainer.setIgnoreDefaultModelOnRedirect(true);
		this.mavContainer.setRedirectModelScenario(true);
		this.mavContainer.addAttribute("name", "value");

		assertThat(this.mavContainer.getModel().size()).isEqualTo(1);
		assertThat(this.mavContainer.getModel().get("name")).isEqualTo("value");
	}


}
