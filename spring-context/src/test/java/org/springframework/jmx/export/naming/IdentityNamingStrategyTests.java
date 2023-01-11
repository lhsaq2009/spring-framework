package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.junit.jupiter.api.Test;

import org.springframework.jmx.JmxTestBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 */
public class IdentityNamingStrategyTests {

	@Test
	public void naming() throws MalformedObjectNameException {
		JmxTestBean bean = new JmxTestBean();
		IdentityNamingStrategy strategy = new IdentityNamingStrategy();
		ObjectName objectName = strategy.getObjectName(bean, "null");
		assertThat(objectName.getDomain()).as("Domain is incorrect").isEqualTo(bean.getClass().getPackage().getName());
		assertThat(objectName.getKeyProperty(IdentityNamingStrategy.TYPE_KEY)).as("Type property is incorrect").isEqualTo(ClassUtils.getShortName(bean.getClass()));
		assertThat(objectName.getKeyProperty(IdentityNamingStrategy.HASH_CODE_KEY)).as("HashCode property is incorrect").isEqualTo(ObjectUtils.getIdentityHexString(bean));
	}

}
