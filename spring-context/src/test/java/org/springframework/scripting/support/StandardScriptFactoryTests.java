package org.springframework.scripting.support;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.springframework.aop.support.AopUtils;
import org.springframework.aop.target.dynamic.Refreshable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scripting.Messenger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link StandardScriptFactory} (lang:std) tests for JavaScript.
 *
 * @author Juergen Hoeller
 * @since 4.2
 */
public class StandardScriptFactoryTests {

	@Test
	public void testJsr223FromTagWithInterface() throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("jsr223-with-xsd.xml", getClass());
		assertThat(Arrays.asList(ctx.getBeanNamesForType(Messenger.class)).contains("messengerWithInterface")).isTrue();
		Messenger messenger = (Messenger) ctx.getBean("messengerWithInterface");
		assertThat(AopUtils.isAopProxy(messenger)).isFalse();
		assertThat(messenger.getMessage()).isEqualTo("Hello World!");
	}

	@Test
	public void testRefreshableJsr223FromTagWithInterface() throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("jsr223-with-xsd.xml", getClass());
		assertThat(Arrays.asList(ctx.getBeanNamesForType(Messenger.class)).contains("refreshableMessengerWithInterface")).isTrue();
		Messenger messenger = (Messenger) ctx.getBean("refreshableMessengerWithInterface");
		assertThat(AopUtils.isAopProxy(messenger)).isTrue();
		boolean condition = messenger instanceof Refreshable;
		assertThat(condition).isTrue();
		assertThat(messenger.getMessage()).isEqualTo("Hello World!");
	}

	@Test
	public void testInlineJsr223FromTagWithInterface() throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("jsr223-with-xsd.xml", getClass());
		assertThat(Arrays.asList(ctx.getBeanNamesForType(Messenger.class)).contains("inlineMessengerWithInterface")).isTrue();
		Messenger messenger = (Messenger) ctx.getBean("inlineMessengerWithInterface");
		assertThat(AopUtils.isAopProxy(messenger)).isFalse();
		assertThat(messenger.getMessage()).isEqualTo("Hello World!");
	}

}
