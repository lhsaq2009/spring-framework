package org.springframework.aop.framework.autoproxy;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.Nullable;

/**
 * 仅考虑基础结构顾问程序 Bean 的 Auto-proxy 创建者，忽略任何应用程序定义的顾问程序。
 *
 * Auto-proxy creator that considers infrastructure Advisor beans only,
 * ignoring any application-defined Advisors.
 *
 * @author Juergen Hoeller
 * @since 2.0.7
 *
 * -----------------------------------------------------------------
 *
 * public abstract class AopConfigUtils {
 *
 *     static {
 *         // Set up the escalation list ...    设置升级列表 ...
 *         APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class);		// 优先级最低
 *         APC_PRIORITY_LIST.add(AspectJAwareAdvisorAutoProxyCreator.class);
 *         APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class);		// 优先级最高
 *     }
 *
 * -----------------------------------------------------------------
 * TODO：2023-01-23，UML 类图，重新从 Monodraw 获取 ...
 * -----------------------------------------------------------------
 *
 * 何时初始化：
 * 		==> getBean("internalAutoProxyCreator")
 * 			=>> getSingleton(..)
 *				=>> initializeBean(..)
 *					=>> invokeAwareMethods(beanName, bean);
 *						   ((BeanNameAware) bean).setBeanName(beanName);
 * 					((BeanClassLoaderAware) bean).setBeanClassLoader(bcl);
 * 						((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
 * 						=>> AbstractAdvisorAutoProxyCreator.setBeanFactory(..)
 * 					       	{@link org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator#setBeanFactory}
 * 						    =>> {@link InfrastructureAdvisorAutoProxyCreator#initBeanFactory}
 *
 * <**:annotation-driven ...>
 * @EnableTransactionManagement & @EnableCaching
 */
@SuppressWarnings("serial")
public class InfrastructureAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator {

	@Nullable
	private ConfigurableListableBeanFactory beanFactory;


	@Override
	protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		super.initBeanFactory(beanFactory);
		this.beanFactory = beanFactory;
	}

	@Override
	protected boolean isEligibleAdvisorBean(String beanName) {
		return (this.beanFactory != null && this.beanFactory.containsBeanDefinition(beanName) &&
				this.beanFactory.getBeanDefinition(beanName).getRole() == BeanDefinition.ROLE_INFRASTRUCTURE);
	}

}
