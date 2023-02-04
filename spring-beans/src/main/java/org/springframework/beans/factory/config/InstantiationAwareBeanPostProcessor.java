package org.springframework.beans.factory.config;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.lang.Nullable;

/**
 * Subinterface of {@link BeanPostProcessor} that adds a before-instantiation callback,
 * and a callback after instantiation but before explicit properties are set or
 * autowiring occurs.
 *
 * <p>Typically used to suppress default instantiation for specific target beans,
 * for example to create proxies with special TargetSources (pooling targets,
 * lazily initializing targets, etc), or to implement additional injection strategies
 * such as field injection.
 *
 * <p><b>NOTE:</b> This interface is a special purpose interface, mainly for
 * internal use within the framework. It is recommended to implement the plain
 * {@link BeanPostProcessor} interface as far as possible, or to derive from
 * {@link InstantiationAwareBeanPostProcessorAdapter} in order to be shielded
 * from extensions to this interface.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @since 1.2
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#setCustomTargetSourceCreators
 * @see org.springframework.aop.framework.autoproxy.target.LazyInitTargetSourceCreator
 *
 * =>> {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#populateBean}
 *     for getBeanPostProcessors() -> if (bp instanceof InstantiationAwareBeanPostProcessor)
 */ // 关于对象实例化前后，以及实例化后，设置 propertyValues 的回调
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * 总结：<br/><br/>
	 *    1) 在对象实例化前，直接返回一个对象 ( 如：代理对象 ) 来代替「通过正常流程创建的对象」，返回 non-null 对象将导致正常的实例化短路<br/><br/>
	 *    2) 如果自定义一个代理对象并返回，那么就会使用定义的代理对象，如果返回 null，Spring 就会走正常实例化流程创建对象<br/><br/>
	 *
	 * <hr><br/>
	 *
	 * Apply this BeanPostProcessor <i>before the target bean gets instantiated</i>.
	 * The returned bean object may be a proxy to use instead of the target bean,
	 * effectively suppressing default instantiation of the target bean.<br/><br/>
	 *
	 * 在目标 Bean 实例化之前，应用此 BeanPostProcessor。
	 * 本方法返回的 Bean 对象，可以是要使用的代理而不是目标 Bean，
	 * 从而有效地抑制了目标 Bean 的默认实例化。<br/><br/>
	 *
	 * <hr>
	 *
	 * <p>If a non-null object is returned by this method, the bean creation process
	 * will be short-circuited. The only further processing applied is the
	 * {@link #postProcessAfterInitialization} callback from the configured
	 * {@link BeanPostProcessor BeanPostProcessors}.
	 * <p>This callback will be applied to bean definitions with their bean class,
	 * as well as to factory-method definitions in which case the returned bean type
	 * will be passed in here.<br/><br/>
	 *
	 * 如果此方法返回 non-null 对象，则 Bean 创建过程将短路。应用的唯一进一步处理是来自已
	 * 配置的 BeanPostProcessor.postProcessAfterlnitialization 回调。<br/><br/>
	 *
	 * 此回调将应用于 Bean definition 及其 Bean Class，以及 factory-method definitions，在这种情况下，返回的
	 * Bean 类型将在此处传入。
	 *
	 * <hr>
	 *
	 * <p>Post-processors may implement the extended
	 * {@link SmartInstantiationAwareBeanPostProcessor} interface in order
	 * to predict the type of the bean object that they are going to return here.
	 * <p>The default implementation returns {@code null}.<br/><br/>
	 *
	 * Post-processors 可以实现扩展的 SmartinstantiationAwareBeanPostProcessor 接口，以便预测它们将在此处返回的 Bean 对象的类型。
	 * 默认实现返回 null。<br/><br/>
	 *
	 * <hr>
	 *
	 * @param beanClass the class of the bean to be instantiated
	 * @param beanName the name of the bean
	 * @return the bean object to expose instead of a default instance of the target bean,
	 * or {@code null} to proceed with default instantiation
	 *
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessAfterInstantiation
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#getBeanClass()
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#getFactoryMethodName()
	 *
	 * =>> AbstractAutowireCapableBeanFactory.createBean(..)}
	 * 	   (实例化前) -> Object bean = resolveBeforeInstantiation(beanName, mbdToUse)
	 * 	   =>> AbstractAutowireCapableBeanFactory.resolveBeforeInstantiation}
	 * 	   	   bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName)
	 * 	   	   =>> AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsBeforeInstantiation}
	 * 	   	   	   if (bp instanceof InstantiationAwareBeanPostProcessor) -> ibp.postProcessBeforeInstantiation(beanClass, beanName)
	 * 	   ...
	 * 	   (去实例化) -> Object beanInstance = doCreateBean(beanName, mbdToUse, args)
	 * ----------------------------------------------------------------------------
	 * 在对象实例化前，直接返回一个对象，造成短路；
	 */
	@Nullable
	default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	/**
	 * 在对象实例化完毕执行 {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#populateBean} 填充属性之前，
	 * 如果返回 false 则 Spring 不再对对应的 bean 实例进行自动依赖注入
	 *
	 * Perform operations after the bean has been instantiated, via a constructor or factory method,
	 * but before Spring property population (from explicit properties or autowiring) occurs.
	 * <p>This is the ideal callback for performing custom field injection on the given bean
	 * instance, right before Spring's autowiring kicks in.
	 * <p>The default implementation returns {@code true}.
	 * @param bean the bean instance created, with properties not having been set yet
	 * @param beanName the name of the bean
	 * @return {@code true} if properties should be set on the bean; {@code false}
	 * if property population should be skipped. Normal implementations should return {@code true}.
	 * Returning {@code false} will also prevent any subsequent InstantiationAwareBeanPostProcessor
	 * instances being invoked on this bean instance.
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessBeforeInstantiation
	 */
	default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return true;
	}

	/**
	 * Post-process the given property values before the factory applies them
	 * to the given bean, without any need for property descriptors.
	 * <p>Implementations should return {@code null} (the default) if they provide a custom
	 * {@link #postProcessPropertyValues} implementation, and {@code pvs} otherwise.
	 * In a future version of this interface (with {@link #postProcessPropertyValues} removed),
	 * the default implementation will return the given {@code pvs} as-is directly.
	 * @param pvs the property values that the factory is about to apply (never {@code null})
	 * @param bean the bean instance created, but whose properties have not yet been set
	 * @param beanName the name of the bean
	 * @return the actual property values to apply to the given bean (can be the passed-in
	 * PropertyValues instance), or {@code null} which proceeds with the existing properties
	 * but specifically continues with a call to {@link #postProcessPropertyValues}
	 * (requiring initialized {@code PropertyDescriptor}s for the current bean class)
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @since 5.1
	 * @see #postProcessPropertyValues
	 */
	@Nullable
	default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
			throws BeansException {

		return null;
	}

	/**
	 * 这里是在 Spring 处理完默认的成员属性，应用到指定的 bean 之前进行回调，可以用来检查和修改属性，
	 * 最终返回的 PropertyValues 会应用到 bean 中 @Autowired、@Resource 等就是根据这个回调来实现最终注入依赖的属性的。<br/><br/>
	 * <hr><br/>
	 *
	 * Post-process the given property values before the factory applies them
	 * to the given bean. Allows for checking whether all dependencies have been
	 * satisfied, for example based on a "Required" annotation on bean property setters.
	 * <p>Also allows for replacing the property values to apply, typically through
	 * creating a new MutablePropertyValues instance based on the original PropertyValues,
	 * adding or removing specific values.
	 * <p>The default implementation returns the given {@code pvs} as-is.
	 * @param pvs the property values that the factory is about to apply (never {@code null})
	 * @param pds the relevant property descriptors for the target bean (with ignored
	 * dependency types - which the factory handles specifically - already filtered out)
	 * @param bean the bean instance created, but whose properties have not yet been set
	 * @param beanName the name of the bean
	 * @return the actual property values to apply to the given bean (can be the passed-in
	 * PropertyValues instance), or {@code null} to skip property population
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessProperties
	 * @see org.springframework.beans.MutablePropertyValues
	 * @deprecated as of 5.1, in favor of {@link #postProcessProperties(PropertyValues, Object, String)}
	 */
	@Deprecated
	@Nullable
	default PropertyValues postProcessPropertyValues(
			PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

		return pvs;
	}

}
