package org.springframework.jmx.export.naming;

import javax.management.ObjectName;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 */
public abstract class AbstractNamingStrategyTests {

	@Test
	public void naming() throws Exception {
		ObjectNamingStrategy strat = getStrategy();
		ObjectName objectName = strat.getObjectName(getManagedResource(), getKey());
		assertThat(getCorrectObjectName()).isEqualTo(objectName.getCanonicalName());
	}

	protected abstract ObjectNamingStrategy getStrategy() throws Exception;

	protected abstract Object getManagedResource() throws Exception;

	protected abstract String getKey();

	protected abstract String getCorrectObjectName();

}
