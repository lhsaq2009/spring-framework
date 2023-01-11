package org.springframework.jndi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public class JndiTemplateEditorTests {

	@Test
	public void testNullIsIllegalArgument() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new JndiTemplateEditor().setAsText(null));
	}

	@Test
	public void testEmptyStringMeansNullEnvironment() {
		JndiTemplateEditor je = new JndiTemplateEditor();
		je.setAsText("");
		JndiTemplate jt = (JndiTemplate) je.getValue();
		assertThat(jt.getEnvironment() == null).isTrue();
	}

	@Test
	public void testCustomEnvironment() {
		JndiTemplateEditor je = new JndiTemplateEditor();
		// These properties are meaningless for JNDI, but we don't worry about that:
		// the underlying JNDI implementation will throw exceptions when the user tries
		// to look anything up
		je.setAsText("jndiInitialSomethingOrOther=org.springframework.myjndi.CompleteRubbish\nfoo=bar");
		JndiTemplate jt = (JndiTemplate) je.getValue();
		assertThat(jt.getEnvironment().size() == 2).isTrue();
		assertThat(jt.getEnvironment().getProperty("jndiInitialSomethingOrOther").equals("org.springframework.myjndi.CompleteRubbish")).isTrue();
		assertThat(jt.getEnvironment().getProperty("foo").equals("bar")).isTrue();
	}

}
