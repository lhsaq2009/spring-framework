package org.springframework.aop.aspectj.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.reflect.PerClauseKind;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 帮助程序，用于从 BeanFactory 检索 @AspectJ bean 并基于它们构建 Spring Advisor，以便与自动代理一起使用。<br/><br/>
 *
 * Helper for retrieving @AspectJ beans from a BeanFactory and building
 * Spring Advisors based on them, for use with auto-proxying.
 *
 * @author Juergen Hoeller
 * @since 2.0.2
 * @see AnnotationAwareAspectJAutoProxyCreator
 */
public class BeanFactoryAspectJAdvisorsBuilder {		// 用于扫描 @Aspect 切面类，解析切面方法

	private final ListableBeanFactory beanFactory;

	private final AspectJAdvisorFactory advisorFactory;	//

	@Nullable
	private volatile List<String> aspectBeanNames;		// 所有 切面类's 名字

	/**
	 * {@link BeanFactoryAspectJAdvisorsBuilder#buildAspectJAdvisors}
	 * =>> this.advisorsCache.put(beanName, classAdvisors);
	 *
	 * ---------------------
	 *
	 * 按照切面类，分组缓存切面方法：
	 *
	 * this.advisorsCache = {ConcurrentHashMap@4136}  size = 1
	 * 		"loggingAspect" -> {ArrayList@4176}  size = 1
	 * 			key   = "loggingAspect"
	 * 			value = {ArrayList@4176}  size = 1
	 * 				0 = {InstantiationModelAwarePointcutAdvisorImpl@4202} "InstantiationModelAwarePointcutAdvisor:
	 * 				expression [updateUserSuccess_JointPointExp()];
	 * 				advice method [public void org.example.beans.LoggingAspect.myBeforeMethod(org.aspectj.lang.JoinPoint)];
	 * 				perClauseKind=SINGLETON"
 	 */
	private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap<>();

	private final Map<String, MetadataAwareAspectInstanceFactory> aspectFactoryCache = new ConcurrentHashMap<>();


	/**
	 * Create a new BeanFactoryAspectJAdvisorsBuilder for the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to scan
	 */
	public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory) {
		this(beanFactory, new ReflectiveAspectJAdvisorFactory(beanFactory));
	}

	/**
	 * Create a new BeanFactoryAspectJAdvisorsBuilder for the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to scan
	 * @param advisorFactory the AspectJAdvisorFactory to build each Advisor with
	 */
	public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory, AspectJAdvisorFactory advisorFactory) {
		Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
		Assert.notNull(advisorFactory, "AspectJAdvisorFactory must not be null");
		this.beanFactory = beanFactory;
		this.advisorFactory = advisorFactory;
	}


	/**
	 * 在当前 Bean factory 中查找 @AspectJ 的 aspect bean，并返回到代表它们的 Spring AOP Advisors 列表。
	 * 为每个 AspectJ advice 方法创建一个 Spring Advisor。<br/><br/>
	 *
	 * Look for AspectJ-annotated aspect beans in the current bean factory,
	 * and return to a list of Spring AOP Advisors representing them.
	 * <p>Creates a Spring Advisor for each AspectJ advice method.
	 * @return the list of {@link org.springframework.aop.Advisor} beans
	 * @see #isEligibleBean
	 */
	public List<Advisor> buildAspectJAdvisors() {					//
		List<String> aspectNames = this.aspectBeanNames;			// 所有 切面类's 名字

		if (aspectNames == null) {
			synchronized (this) {
				aspectNames = this.aspectBeanNames;
				if (aspectNames == null) {

					List<Advisor> advisors = new ArrayList<>();		// 所有 切面类's 所有切面方法
					aspectNames = new ArrayList<>();				// 所有 切面类's 名字

					// 查询所有的 Object.class 的 BeanNames
					String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
							this.beanFactory, Object.class, true, false);
					for (String beanName : beanNames) {				// Core
						// CASE 1. beanName.contains("loggingAspect")
						if (!isEligibleBean(beanName)) {
							continue;
						}
						// We must be careful not to instantiate beans eagerly as in this case they
						// would be cached by the Spring container but would not have been weaved.
						Class<?> beanType = this.beanFactory.getType(beanName, false);
						if (beanType == null) {
							continue;
						}

						// 判断，该类是否启用了 @Aspect 类注解
						if (this.advisorFactory.isAspect(beanType)) {
							aspectNames.add(beanName);		//
							AspectMetadata amd = new AspectMetadata(beanType, beanName);		//
							if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
								MetadataAwareAspectInstanceFactory factory =
										new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);			//
								// 封装收集该类的所有切面方法
								List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);				// =>>
								if (this.beanFactory.isSingleton(beanName)) {
									this.advisorsCache.put(beanName, classAdvisors);	//
								}
								else {
									this.aspectFactoryCache.put(beanName, factory);
								}
								advisors.addAll(classAdvisors);
							}
							else {
								// Per target or per this.
								if (this.beanFactory.isSingleton(beanName)) {
									throw new IllegalArgumentException("Bean with name '" + beanName +
											"' is a singleton, but aspect instantiation model is not singleton");
								}
								MetadataAwareAspectInstanceFactory factory =
										new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
								this.aspectFactoryCache.put(beanName, factory);
								advisors.addAll(this.advisorFactory.getAdvisors(factory));
							}
						}
					}
					this.aspectBeanNames = aspectNames;
					return advisors;
				}
			}
		}

		if (aspectNames.isEmpty()) {
			return Collections.emptyList();
		}
		List<Advisor> advisors = new ArrayList<>();
		for (String aspectName : aspectNames) {
			List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
			if (cachedAdvisors != null) {
				advisors.addAll(cachedAdvisors);
			}
			else {
				MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
				advisors.addAll(this.advisorFactory.getAdvisors(factory));
			}
		}
		return advisors;
	}

	/**
	 * Return whether the aspect bean with the given name is eligible.
	 * @param beanName the name of the aspect bean
	 * @return whether the bean is eligible
	 */
	protected boolean isEligibleBean(String beanName) {
		return true;
	}

}
