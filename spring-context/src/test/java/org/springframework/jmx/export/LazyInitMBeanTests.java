package org.springframework.jmx.export;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jmx.support.ObjectNameManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class LazyInitMBeanTests {

	@Test
	public void invokeOnLazyInitBean() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("org/springframework/jmx/export/lazyInit.xml");
		assertThat(ctx.getBeanFactory().containsSingleton("testBean")).isFalse();
		assertThat(ctx.getBeanFactory().containsSingleton("testBean2")).isFalse();
		try {
			MBeanServer server = (MBeanServer) ctx.getBean("server");
			ObjectName oname = ObjectNameManager.getInstance("bean:name=testBean2");
			String name = (String) server.getAttribute(oname, "Name");
			assertThat(name).as("Invalid name returned").isEqualTo("foo");
		}
		finally {
			ctx.close();
		}
	}

}
