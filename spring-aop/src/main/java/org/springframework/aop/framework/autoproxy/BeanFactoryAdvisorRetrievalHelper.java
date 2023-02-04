package org.springframework.aop.framework.autoproxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 用于从 BeanFactory 检索 standard Spring Advisors，用于自动代理。
 *
 * Helper for retrieving standard Spring Advisors from a BeanFactory,
 * for use with auto-proxying.
 *
 * @author Juergen Hoeller
 * @since 2.0.2
 * @see AbstractAdvisorAutoProxyCreator
 */
public class BeanFactoryAdvisorRetrievalHelper {	//

	private static final Log logger = LogFactory.getLog(BeanFactoryAdvisorRetrievalHelper.class);

	private final ConfigurableListableBeanFactory beanFactory;

	/**
	 * 何时写入：
	 * =>> {@link BeanFactoryAdvisorRetrievalHelper#findAdvisorBeans}
	 *     =>> 扫描手机 Advisor.class 的子类
	 *
	 * --------------------------------------------------------------
	 *
	 * cachedAdvisorBeanNames = {String[2]@4110} ["org.springframe...", "myPointcutAdvis..."]
	 * 		0 = "org.springframework.transaction.config.internalTransactionAdvisor"
	 * 		1 = "myPointcutAdvisor"
	 */
	@Nullable
	private volatile String[] cachedAdvisorBeanNames;


	/**
	 * Create a new BeanFactoryAdvisorRetrievalHelper for the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to scan
	 */
	public BeanFactoryAdvisorRetrievalHelper(ConfigurableListableBeanFactory beanFactory) {
		Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
		this.beanFactory = beanFactory;
	}


	/**
	 * 查找 beanFactory 中符合条件 (BeanDefinition.ROLE_INFRASTRUCTURE)
	 * 的 Advisor Bean，忽略 FactoryBeans & 排除正在创建的 Bean
	 *
	 * Find all eligible Advisor beans in the current bean factory,
	 * ignoring FactoryBeans and excluding beans that are currently in creation.
	 * @return the list of {@link org.springframework.aop.Advisor} beans
	 * @see #isEligibleBean
	 *
	 * CASE 1：第一次：getBean("dataSource") && processor = {InfrastructureAdvisorAutoProxyCreator@4179}
	 * 		   =>> 查找 (BeanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE) 的 Advisor.class
	 * 		   	   this.beanFactory.getBean("internalTransactionAdvisor", Advisor.class) -->
	 * 		   	   未实例化，去创建：BeanFactoryTransactionAttributeSourceAdvisor
	 * 		   	   =>> 期间因填充属性 ( BeanFactoryTransactionAttributeSourceAdvisor.transactionAttributeSource )，而再次进入本方法
	 * 		   	   	   但此时 this.beanFactory.isCurrentlyInCreation("internalTransactionAdvisor") 尚处于创建中，会返回 true
	 *
	 * 		   // advisors = {ArrayList@4457}  size = 1
	 * 		   //     0 = {BeanFactoryTransactionAttributeSourceAdvisor@4471}
	 *		   return advisors;
	 */
	public List<Advisor> findAdvisorBeans() {

		/*
		 * advisorNames = {String[2]@4054} ["org.springframe...", "myPointcutAdvis..."]
		 * 		0 = "org.springframework.transaction.config.internalTransactionAdvisor" -> BeanFactoryTransactionAttributeSourceAdvisor
		 * 		1 = "myPointcutAdvisor"
		 */
		// Determine list of advisor bean names, if not cached already.
		String[] advisorNames = this.cachedAdvisorBeanNames;
		if (advisorNames == null) {
			/*
			 * CASE 1. @Service
			 *         public class Log_Service_Advisor_extend_Orders_44 extends AbstractPointcutAdvisor {..}
			 */
			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the auto-proxy creator apply to them!
			advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(		// 查找所有 Advisor 实现类 & 并缓存
					this.beanFactory, Advisor.class, true, false);
			this.cachedAdvisorBeanNames = advisorNames;								// 缓存起来
		}
		if (advisorNames.length == 0) {
			return new ArrayList<>();
		}

		List<Advisor> advisors = new ArrayList<>();									// 收集 BeanFactory 已注册的 Advisor ( BeanDefinition.getRole = BeanDefinition.ROLE_INFRASTRUCTURE )
		for (String name : advisorNames) {
			if (isEligibleBean(name)) {												// 钩子方法，默认返回 true
				if (this.beanFactory.isCurrentlyInCreation(name)) {					// Skipping currently created advisor
					if (logger.isTraceEnabled()) {
						logger.trace("Skipping currently created advisor '" + name + "'");
					}
				}
				else {
					try {
						// this.beanFactory.getBean(name, Advisor.class) = {BeanFactoryTransactionAttributeSourceAdvisor@4567}
						advisors.add(this.beanFactory.getBean(name, Advisor.class));	// =>>
					}
					catch (BeanCreationException ex) {
						Throwable rootCause = ex.getMostSpecificCause();
						if (rootCause instanceof BeanCurrentlyInCreationException) {
							BeanCreationException bce = (BeanCreationException) rootCause;
							String bceBeanName = bce.getBeanName();
							if (bceBeanName != null && this.beanFactory.isCurrentlyInCreation(bceBeanName)) {
								if (logger.isTraceEnabled()) {
									logger.trace("Skipping advisor '" + name + "' with dependency on currently created bean: " + ex.getMessage());
								}
								// Ignore: indicates a reference back to the bean we're trying to advise.
								// We want to find advisors other than the currently created bean itself.
								continue;
							}
						}
						throw ex;
					}
				}
			}
		}
		return advisors;
	}

	/**
	 * Determine whether the aspect bean with the given name is eligible.
	 * <p>The default implementation always returns {@code true}.
	 * @param beanName the name of the aspect bean
	 * @return whether the bean is eligible
	 */
	protected boolean isEligibleBean(String beanName) {
		return true;
	}

}
