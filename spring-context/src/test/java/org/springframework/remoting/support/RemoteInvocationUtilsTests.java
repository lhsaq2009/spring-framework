package org.springframework.remoting.support;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rick Evans
 */
public class RemoteInvocationUtilsTests {

	@Test
	public void fillInClientStackTraceIfPossibleSunnyDay() throws Exception {
		try {
			throw new IllegalStateException("Mmm");
		}
		catch (Exception ex) {
			int originalStackTraceLngth = ex.getStackTrace().length;
			RemoteInvocationUtils.fillInClientStackTraceIfPossible(ex);
			assertThat(ex.getStackTrace().length > originalStackTraceLngth).as("Stack trace not being filled in").isTrue();
		}
	}

	@Test
	public void fillInClientStackTraceIfPossibleWithNullThrowable() throws Exception {
		// just want to ensure that it doesn't bomb
		RemoteInvocationUtils.fillInClientStackTraceIfPossible(null);
	}

}
