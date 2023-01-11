package org.springframework.web.servlet.tags;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Param}.
 *
 * @author Scott Andrews
 */
public class ParamTests {

	private final Param param = new Param();

	@Test
	public void name() {
		param.setName("name");
		assertThat(param.getName()).isEqualTo("name");
	}

	@Test
	public void value() {
		param.setValue("value");
		assertThat(param.getValue()).isEqualTo("value");
	}

	@Test
	public void nullDefaults() {
		assertThat(param.getName()).isNull();
		assertThat(param.getValue()).isNull();
	}

}
