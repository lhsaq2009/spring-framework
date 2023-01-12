package org.springframework.beans;

/**
 * Interface for strategies that register custom
 * {@link java.beans.PropertyEditor property editors} with a
 * {@link org.springframework.beans.PropertyEditorRegistry property editor registry}.
 *
 * <p>This is particularly useful when you need to use the same set of
 * property editors in several different situations: write a corresponding
 * registrar and reuse that in each case.
 *
 * @author Juergen Hoeller
 * @since 1.2.6
 * @see PropertyEditorRegistry
 * @see java.beans.PropertyEditor
 */
public interface PropertyEditorRegistrar {

	/**
	 * Register custom {@link java.beans.PropertyEditor PropertyEditors} with
	 * the given {@code PropertyEditorRegistry}.
	 * <p>The passed-in registry will usually be a {@link BeanWrapper} or a
	 * {@link org.springframework.validation.DataBinder DataBinder}.
	 * <p>It is expected that implementations will create brand new
	 * {@code PropertyEditors} instances for each invocation of this
	 * method (since {@code PropertyEditors} are not threadsafe).
	 * @param registry the {@code PropertyEditorRegistry} to register the
	 * custom {@code PropertyEditors} with
	 *
	 * <br><p></p>
	 *
	 * 添加数据：
	 * =>> {@link org.springframework.context.support.AbstractApplicationContext#refresh}
	 *     =>> {@link org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory}
	 *         // ConfigurableBeanFactory#addPropertyEditorRegistrar(PropertyEditorRegistrar) --> AbstractBeanFactory#propertyEditorRegistrars 集合
	 * 		   beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
	 *
	 * getBean(..) 反射创建实例后，回调开始注册：
	 * =>> {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean}
	 *     =>> {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#createBeanInstance}
	 *         =>> {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#instantiateBean}
	 *             =>> {@link org.springframework.beans.factory.support.AbstractBeanFactory#initBeanWrapper}
	 *                 =>> {@link org.springframework.beans.factory.support.AbstractBeanFactory#registerCustomEditors}
	 *                         // public class ResourceEditorRegistrar implements PropertyEditorRegistrar
	 *                     =>> {@link org.springframework.beans.support.ResourceEditorRegistrar#registerCustomEditors}
	 *                         =>> 注册 editors 收集到 {@link PropertyEditorRegistrySupport#overriddenDefaultEditors}
	 */
	void registerCustomEditors(PropertyEditorRegistry registry);

}
