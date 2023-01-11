package org.springframework.beans.factory.parsing;

import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.DescriptiveResource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class FailFastProblemReporterTests {

	@Test
	public void testError() throws Exception {
		FailFastProblemReporter reporter = new FailFastProblemReporter();
		assertThatExceptionOfType(BeanDefinitionParsingException.class).isThrownBy(() ->
				reporter.error(new Problem("VGER", new Location(new DescriptiveResource("here")),
						null, new IllegalArgumentException())));
	}

	@Test
	public void testWarn() throws Exception {
		Problem problem = new Problem("VGER", new Location(new DescriptiveResource("here")),
				null, new IllegalArgumentException());

		Log log = mock(Log.class);

		FailFastProblemReporter reporter = new FailFastProblemReporter();
		reporter.setLogger(log);
		reporter.warning(problem);

		verify(log).warn(any(), isA(IllegalArgumentException.class));
	}

}
