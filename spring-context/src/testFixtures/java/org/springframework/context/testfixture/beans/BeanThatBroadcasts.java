package org.springframework.context.testfixture.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Juergen Hoeller
 */
public class BeanThatBroadcasts implements ApplicationContextAware {

	public ApplicationContext applicationContext;

	public int receivedCount;


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		if (applicationContext.getDisplayName().contains("listener")) {
			applicationContext.getBean("listener");
		}
	}

}
