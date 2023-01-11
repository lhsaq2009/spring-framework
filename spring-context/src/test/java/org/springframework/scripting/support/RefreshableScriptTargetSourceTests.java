package org.springframework.scripting.support;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.BeanFactory;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

/**
 * @author Rick Evans
 */
public class RefreshableScriptTargetSourceTests {

	@Test
	public void createWithNullScriptSource() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new RefreshableScriptTargetSource(mock(BeanFactory.class), "a.bean", null, null, false));
	}

}
