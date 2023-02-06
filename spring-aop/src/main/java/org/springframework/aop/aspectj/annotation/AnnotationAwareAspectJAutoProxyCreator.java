package org.springframework.aop.aspectj.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * {@link AspectJAwareAdvisorAutoProxyCreator} subclass that processes all AspectJ
 * annotation aspects in the current application context, as well as Spring Advisors.
 *
 * <p>Any AspectJ annotated classes will automatically be recognized, and their
 * advice applied if Spring AOP's proxy-based model is capable of applying it.
 * This covers method execution joinpoints.
 *
 * <p>If the &lt;aop:include&gt; element is used, only @AspectJ beans with names matched by
 * an include pattern will be considered as defining aspects to use for Spring auto-proxying.
 *
 * <p>Processing of Spring Advisors follows the rules established in
 * {@link org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator}.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.aop.aspectj.annotation.AspectJAdvisorFactory
 *
 * --------------------------------------------------------------------
 *
 * TODO：也是个后置处理器
 * =>> 父类的 {@link org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#createProxy}
 */
@SuppressWarnings("serial")
public class AnnotationAwareAspectJAutoProxyCreator extends AspectJAwareAdvisorAutoProxyCreator {	// TODO：处理 @Aspect ？？

	/**
	 * {@link org.springframework.aop.config.AspectJAutoProxyBeanDefinitionParser#extendBeanDefinition}
	 * =>> addIncludePatterns(element, parserContext, beanDef);
	 * 	   =>> 解析 <aop:include /> -> includePatterns
	 */
	@Nullable
	private List<Pattern> includePatterns;

	@Nullable
	private AspectJAdvisorFactory aspectJAdvisorFactory;

	/**
	 * 赋值来自: {@link #initBeanFactory}
	 * 		    aspectJAdvisorsBuilder = new BeanFactoryAspectJAdvisorsBuilderAdapter(..)
	 * 		    =>> 这个 Adapter 是本类的内部类，并且 extends BeanFactoryAspectJAdvisorsBuilder
	 */
	@Nullable
	private BeanFactoryAspectJAdvisorsBuilder aspectJAdvisorsBuilder;		// core：用于扫描和解析 @Aspect 切面类


	/**
	 * Set a list of regex patterns, matching eligible @AspectJ bean names.
	 * <p>Default is to consider all @AspectJ beans as eligible.
	 */
	public void setIncludePatterns(List<String> patterns) {
		this.includePatterns = new ArrayList<>(patterns.size());
		for (String patternText : patterns) {
			this.includePatterns.add(Pattern.compile(patternText));
		}
	}

	public void setAspectJAdvisorFactory(AspectJAdvisorFactory aspectJAdvisorFactory) {
		Assert.notNull(aspectJAdvisorFactory, "AspectJAdvisorFactory must not be null");
		this.aspectJAdvisorFactory = aspectJAdvisorFactory;
	}

	/**
	 * 何时加载 ？
	 * 加载后，放在哪 ？
	 */
	@Override
	protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		super.initBeanFactory(beanFactory);
		if (this.aspectJAdvisorFactory == null) {
			// Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class)
			this.aspectJAdvisorFactory = new ReflectiveAspectJAdvisorFactory(beanFactory);		// =>>
		}
		this.aspectJAdvisorsBuilder = new BeanFactoryAspectJAdvisorsBuilderAdapter(beanFactory, this.aspectJAdvisorFactory);	// =>>
	}


	@Override
	protected List<Advisor> findCandidateAdvisors() {

		/*
		 * 第一步：收集 Advisor.class 子类
		 *
		 * CASE 1. beanName = "transactionByAnnotation"
		 * 		advisors = {ArrayList@4625}  size = 4
		 * 		0 = {BeanFactoryTransactionAttributeSourceAdvisor@4404}
		 * 			adviceBeanName = "org.springframework.transaction.interceptor.TransactionInterceptor#0"
		 * 		1 = {DefaultBeanFactoryPointcutAdvisor@4177}
		 * 			advice bean 'logXML_AOP_Advisor_Implement_Order_55'"
		 * 		2 = {AspectJPointcutAdvisor@4328}					-- 同一个类的每个方法，都单独封装一个 AspectJPointcutAdvisor 对象
		 * 			aspectName = "logXML_AOP_Aspect_Order_22"
		 * 			methodName = "myXMLBeforeMethod"
		 * 		3 = {Log_Service_Advisor_extend_Orders_44@4332}
		 * 			@Service
		 * 			public class Log_Service_Advisor_extend_Orders_44 extends AbstractPointcutAdvisor
		 */
		List<Advisor> advisors = super.findCandidateAdvisors();							// =>> 收集 Advisor.class 子类：advisorRetrievalHelper.findAdvisorBeans()

		/*
		 * 第二步：收集 @AspectJ 注解的切面类
		 *
		 * 为 bean factory 中的所有 AspectJ aspects 构建 Advisors；
		 * Build Advisors for all AspectJ aspects in the bean factory.
		 */
		if (this.aspectJAdvisorsBuilder != null) {
			advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());		// =>> <aop:aspectj-autoproxy
		}

		/*
		 * TODO：所有的切面方法；??? 类分组 ？？
		 *
		 * CASE 1. beanName = "transactionByAnnotation"
		 * advisors[5] = {InstantiationModelAwarePointcutAdvisorImpl@4192} -- @Aspect
		 * 		aspectName 		= "log_Annotation_Order_33"
		 * 		methodName 		= "myBeforeMethod"
		 * 		...
		 */
		return advisors;
	}

	@Override
	protected boolean isInfrastructureClass(Class<?> beanClass) {
		// Previously we setProxyTargetClass(true) in the constructor, but that has too
		// broad an impact. Instead we now override isInfrastructureClass to avoid proxying
		// aspects. I'm not entirely happy with that as there is no good reason not
		// to advise aspects, except that it causes advice invocation to go through a
		// proxy, and if the aspect implements e.g the Ordered interface it will be
		// proxied by that interface and fail at runtime as the advice method is not
		// defined on the interface. We could potentially relax the restriction about
		// not advising aspects in the future.
		return (super.isInfrastructureClass(beanClass) ||
				(this.aspectJAdvisorFactory != null && this.aspectJAdvisorFactory.isAspect(beanClass)));
	}

	/**
	 * ==> BeanFactoryAspectJAdvisorsBuilder.buildAspectJAdvisors(..)
	 *     for (String beanName : beanNames) -> if (!isEligibleBean(beanName))
	 *     ==> AnnotationAwareAspectJAutoProxyCreator.this.isEligibleAspectBean(beanName); <br/><br/>
	 *
	 * <hr><br/>
	 *
	 * 检查给定的 aspect Bean 是否符合自动代理的条件。Check whether the given aspect bean is eligible for auto-proxying.
	 * <p>If no &lt;aop:include&gt; elements were used then "includePatterns" will be
	 * {@code null} and all beans are included. If "includePatterns" is non-null,
	 * then one of the patterns must match.
	 */
	protected boolean isEligibleAspectBean(String beanName) {		// core：@Aspect 类，是否可代理别人
		if (this.includePatterns == null) {
			return true;
		}
		else {
			for (Pattern pattern : this.includePatterns) {
				if (pattern.matcher(beanName).matches()) {
					return true;
				}
			}
			return false;
		}
	}


	/**
	 * Subclass of BeanFactoryAspectJAdvisorsBuilderAdapter that delegates to
	 * surrounding AnnotationAwareAspectJAutoProxyCreator facilities.
	 */
	private class BeanFactoryAspectJAdvisorsBuilderAdapter extends BeanFactoryAspectJAdvisorsBuilder {

		public BeanFactoryAspectJAdvisorsBuilderAdapter(
				ListableBeanFactory beanFactory, AspectJAdvisorFactory advisorFactory) {

			super(beanFactory, advisorFactory);
		}
		// ==> BeanFactoryAspectJAdvisorsBuilder.buildAspectJAdvisors(..)
		//     for (String beanName : beanNames) -> if (!isEligibleBean(beanName))
		@Override
		protected boolean isEligibleBean(String beanName) {
			return AnnotationAwareAspectJAutoProxyCreator.this.isEligibleAspectBean(beanName);
		}
	}

}
