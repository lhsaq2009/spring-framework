package org.springframework.test.web.servlet.result;

import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.StubMvcResult;
import org.springframework.web.servlet.FlashMap;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Craig Walls
 */
public class FlashAttributeResultMatchersTests {

	@Test
	public void attributeExists() throws Exception {
		new FlashAttributeResultMatchers().attributeExists("good").match(getStubMvcResult());
	}

	@Test
	public void attributeExists_doesntExist() throws Exception {
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
				new FlashAttributeResultMatchers().attributeExists("bad").match(getStubMvcResult()));
	}

	@Test
	public void attribute() throws Exception {
		new FlashAttributeResultMatchers().attribute("good", "good").match(getStubMvcResult());
	}

	@Test
	public void attribute_incorrectValue() throws Exception {
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
				new FlashAttributeResultMatchers().attribute("good", "not good").match(getStubMvcResult()));
	}

	private StubMvcResult getStubMvcResult() {
		FlashMap flashMap = new FlashMap();
		flashMap.put("good", "good");
		StubMvcResult mvcResult = new StubMvcResult(null, null, null, null, null, flashMap, null);
		return mvcResult;
	}

}
