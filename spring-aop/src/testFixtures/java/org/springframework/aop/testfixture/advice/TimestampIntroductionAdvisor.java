package org.springframework.aop.testfixture.advice;

import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.aop.testfixture.interceptor.TimestampIntroductionInterceptor;

/**
 * @author Rod Johnson
 */
@SuppressWarnings("serial")
public class TimestampIntroductionAdvisor extends DefaultIntroductionAdvisor {

	public TimestampIntroductionAdvisor() {
		super(new DelegatingIntroductionInterceptor(new TimestampIntroductionInterceptor()));
	}

}
