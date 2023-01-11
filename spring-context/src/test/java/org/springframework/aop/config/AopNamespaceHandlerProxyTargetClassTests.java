package org.springframework.aop.config;

import org.junit.jupiter.api.Test;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.testfixture.beans.ITestBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 * @author Chris Beams
 */
public class AopNamespaceHandlerProxyTargetClassTests extends AopNamespaceHandlerTests {

	@Test
	public void testIsClassProxy() {
		ITestBean bean = getTestBean();
		assertThat(AopUtils.isCglibProxy(bean)).as("Should be a CGLIB proxy").isTrue();
		assertThat(((Advised) bean).isExposeProxy()).as("Should expose proxy").isTrue();
	}

}
