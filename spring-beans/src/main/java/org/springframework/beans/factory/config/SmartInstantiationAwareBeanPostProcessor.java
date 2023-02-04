package org.springframework.beans.factory.config;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * Extension of the {@link InstantiationAwareBeanPostProcessor} interface,
 * adding a callback for predicting the eventual type of a processed bean.<br/><br/>
 *
 * {@link InstantiationAwareBeanPostProcessor} 接口的扩展，添加用于 预测已处理 Bean 的最终类型的回调。<br/><br/>
 *
 * <hr>
 *
 * <p><b>NOTE:</b> This interface is a special purpose interface, mainly for
 * internal use within the framework. In general, application-provided
 * post-processors should simply implement the plain {@link BeanPostProcessor}
 * interface or derive from the {@link InstantiationAwareBeanPostProcessorAdapter}
 * class. New methods might be added to this interface even in point releases.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see InstantiationAwareBeanPostProcessorAdapter
 */
	public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

	/**
	 * 预测 {@link #postProcessBeforeInstantiation} 最终返回的 Bean 的类型。
	 * 默认实现返回 null。<br/><br/>
	 *
	 * Predict the type of the bean to be eventually returned from this
	 * processor's {@link #postProcessBeforeInstantiation} callback.
	 * <p>The default implementation returns {@code null}. <br/><br/>
	 *
	 * <hr>
	 *
	 * @param beanClass the raw class of the bean
	 * @param beanName the name of the bean
	 * @return the type of the bean, or {@code null} if not predictable
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	@Nullable
	default Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	/**
	 * 确定要用于给定 Bean 的 候选构造函数。 默认实现返回 null。<br/><br/>
	 *
	 * <hr><br/>
	 *
	 * Determine the candidate constructors to use for the given bean.
	 * <p>The default implementation returns {@code null}.
	 * @param beanClass the raw class of the bean (never {@code null})
	 * @param beanName the name of the bean
	 * @return the candidate constructors, or {@code null} if none specified
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	@Nullable
	default Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName)
			throws BeansException {

		return null;
	}

	/**
	 * Obtain a reference for early access to the specified bean,
	 * typically for the purpose of resolving a circular reference.<br/><br/>
	 *
	 * 提前把可能未初始化完毕的 bean 提前暴漏出去，解决循环引用 ( Circular Reference )
	 * 获取 reference 以便提前访问 指定的 Bean，通常用于解析循环 (circular reference) 引用。<br/><br/>
	 *
	 * 存储位置：this.singletonFactories.put(beanName, singletonFactory);
	 *         =>> {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean}
	 *             addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
	 *             =>> 存储位置：this.singletonFactories.put(beanName, singletonFactory);
	 *
	 * 何时回调：{@link DefaultSingletonBeanRegistry#getSingleton(String, boolean)}
	 *         ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
	 *         singletonObject = singletonFactory.getObject();
	 *
	 * <hr>
	 *
	 * <p>This callback gives post-processors a chance to expose a wrapper
	 * early - that is, before the target bean instance is fully initialized.
	 * The exposed object should be equivalent to the what
	 * {@link #postProcessBeforeInitialization} / {@link #postProcessAfterInitialization}
	 * would expose otherwise. Note that the object returned by this method will
	 * be used as bean reference unless the post-processor returns a different
	 * wrapper from said post-process callbacks. In other words: Those post-process
	 * callbacks may either eventually expose the same reference or alternatively
	 * return the raw bean instance from those subsequent callbacks (if the wrapper
	 * for the affected bean has been built for a call to this method already,
	 * it will be exposes as final bean reference by default).
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the raw bean instance
	 * @param beanName the name of the bean
	 * @return the object to expose as bean reference
	 * (typically with the passed-in bean instance as default)
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
