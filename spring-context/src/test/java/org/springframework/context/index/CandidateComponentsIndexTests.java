package org.springframework.context.index;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CandidateComponentsIndex}.
 *
 * @author Stephane Nicoll
 */
public class CandidateComponentsIndexTests {

	@Test
	public void getCandidateTypes() {
		CandidateComponentsIndex index = new CandidateComponentsIndex(
				Collections.singletonList(createSampleProperties()));
		Set<String> actual = index.getCandidateTypes("com.example.service", "service");
		assertThat(actual).contains("com.example.service.One",
				"com.example.service.sub.Two", "com.example.service.Three");
	}

	@Test
	public void getCandidateTypesSubPackage() {
		CandidateComponentsIndex index = new CandidateComponentsIndex(
				Collections.singletonList(createSampleProperties()));
		Set<String> actual = index.getCandidateTypes("com.example.service.sub", "service");
		assertThat(actual).contains("com.example.service.sub.Two");
	}

	@Test
	public void getCandidateTypesSubPackageNoMatch() {
		CandidateComponentsIndex index = new CandidateComponentsIndex(
				Collections.singletonList(createSampleProperties()));
		Set<String> actual = index.getCandidateTypes("com.example.service.none", "service");
		assertThat(actual).isEmpty();
	}

	@Test
	public void getCandidateTypesNoMatch() {
		CandidateComponentsIndex index = new CandidateComponentsIndex(
				Collections.singletonList(createSampleProperties()));
		Set<String> actual = index.getCandidateTypes("com.example.service", "entity");
		assertThat(actual).isEmpty();
	}

	@Test
	public void mergeCandidateStereotypes() {
		CandidateComponentsIndex index = new CandidateComponentsIndex(Arrays.asList(
				createProperties("com.example.Foo", "service"),
				createProperties("com.example.Foo", "entity")));
		assertThat(index.getCandidateTypes("com.example", "service"))
				.contains("com.example.Foo");
		assertThat(index.getCandidateTypes("com.example", "entity"))
				.contains("com.example.Foo");
	}

	private static Properties createProperties(String key, String stereotypes) {
		Properties properties = new Properties();
		properties.put(key, String.join(",", stereotypes));
		return properties;
	}

	private static Properties createSampleProperties() {
		Properties properties = new Properties();
		properties.put("com.example.service.One", "service");
		properties.put("com.example.service.sub.Two", "service");
		properties.put("com.example.service.Three", "service");
		properties.put("com.example.domain.Four", "entity");
		return properties;
	}

}
