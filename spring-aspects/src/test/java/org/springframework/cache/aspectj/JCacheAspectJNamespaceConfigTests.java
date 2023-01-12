package org.springframework.cache.aspectj;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.contextsupport.testfixture.jcache.AbstractJCacheAnnotationTests;

/**
 * @author Stephane Nicoll
 */
public class JCacheAspectJNamespaceConfigTests extends AbstractJCacheAnnotationTests {

	@Override
	protected ApplicationContext getApplicationContext() {
		return new GenericXmlApplicationContext(
				"/org/springframework/cache/config/annotation-jcache-aspectj.xml");
	}

}
