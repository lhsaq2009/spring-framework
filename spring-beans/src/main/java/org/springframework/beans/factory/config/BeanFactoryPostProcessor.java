package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * Factory hook that allows for custom modification of an application context's
 * bean definitions, adapting the bean property values of the context's underlying bean factory.<br/><br/>
 *
 * Factory hook -> 允许对 应用程序上下文 的 Bean definitions 进行 自定义修改，从而调整「上下文的底层 bean factory」的 Bean 属性值。<br/><br/>
 *
 * <hr>
 *
 * <p>Useful for custom config files targeted at system administrators that
 * override bean properties configured in the application context. See
 * {@link PropertyResourceConfigurer} and its concrete implementations for
 * out-of-the-box solutions that address such configuration needs. <br/><br/>
 *
 * 对于面向「在应用程序上下文中 覆盖配置的 Bean properties」的系统管理员的「自定义配置文件」非常有用。
 * 请参阅 PropertyResourceConfigurer 及其具体实现，了解满足此类配置需求的现成解决方案。<br/><br/>
 *
 * <hr>
 *
 * <p>A {@code BeanFactoryPostProcessor} may interact with and modify bean
 * definitions, but never bean instances. Doing so may cause premature bean
 * instantiation, violating the container and causing unintended side-effects.
 * If bean instance interaction is required, consider implementing
 * {@link BeanPostProcessor} instead.<br/><br/>
 *
 * BeanFactoryPostProcessor 可以与 Bean 定义交互并修改它们，但从来不能修改 Bean
 * 实例。这样做可能会导致过早的 Bean 实例化，违反容器并导致意外的副作用。如果需
 * 要 Bean 实例交互，请考虑改为实现 BeanPostProcessor。<br/><br/>
 *
 * <hr>
 *
 * <h3>Registration (注册、登记)</h3>
 * <p>An {@code ApplicationContext} auto-detects {@code BeanFactoryPostProcessor}
 * beans in its bean definitions and applies them before any other beans get created.
 * A {@code BeanFactoryPostProcessor} may also be registered programmatically
 * with a {@code ConfigurableApplicationContext}. <br/><br/>
 *
 * ApplicationContext 在其 bean definitions 中 自动检测 BeanFactoryPostProcessor bean，并在创建任何其它 bean 之前应用它们。
 * BeanFactoryPostProcessor 也可以以编程方式注册到 ContigurableApplicationContext。<br/><br/>
 *
 * <hr>
 *
 * <h3>Ordering ( 顺序 )</h3>
 * <p>{@code BeanFactoryPostProcessor} beans that are autodetected in an
 * {@code ApplicationContext} will be ordered according to
 * {@link org.springframework.core.PriorityOrdered} and
 * {@link org.springframework.core.Ordered} semantics. In contrast,
 * {@code BeanFactoryPostProcessor} beans that are registered programmatically
 * with a {@code ConfigurableApplicationContext} will be applied in the order of
 * registration; any ordering semantics expressed through implementing the
 * {@code PriorityOrdered} or {@code Ordered} interface will be ignored for
 * programmatically registered post-processors. Furthermore, the
 * {@link org.springframework.core.annotation.Order @Order} annotation is not
 * taken into account for {@code BeanFactoryPostProcessor} beans.<br/><br/>
 *
 * 在 ApplicationContext 中自动检测的 BeanFactoryPostProcessor bean 将根据
 * {@link org.springframework.core.PriorityOrdered} 和 {@link org.springframework.core.Ordered} 语义进行排序。<br/>
 * 相比之下，以编程方式注册到 ConfigurableApplicationContext 的 BeanFactoryPostProcessor bean 将按注册顺序应用;
 * 对于以编程方式注册的后处理器，
 * 将忽略通过实现 PriorityOrdered 或 Ordered 接口表示的任何排序语义。此外，
 * BeanFactoryPostProcessor bean 不考虑 @Order 注释。<br/><br/>
 *
 * <hr>
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 06.07.2003
 * @see BeanPostProcessor
 * @see PropertyResourceConfigurer
 */
@FunctionalInterface
public interface BeanFactoryPostProcessor {

	/**
	 * Modify the application context's internal bean factory after its standard
	 * initialization. All bean definitions will have been loaded, but no beans
	 * will have been instantiated yet. This allows for overriding or adding
	 * properties even to eager-initializing beans.<br/><br/>
	 *
	 * 在「标准初始化」后修改 the application context's 的内部 Bean 工厂。
	 * 所有 bean definitions 已被加载，但尚未实例化任何 bean。
	 * 这允许对 Bean 甚至 eager-initializing bean，进行覆盖 或 添加属性<br/><br/>
	 *
	 * 在实例化之前，Spring 允许自定义扩展来改变 Bean 的定义，定义一旦变了，后面的实例也就变了该方法的实现中，主要用来对 Bean 定义做一些改变
	 *
	 * @param beanFactory the bean factory used by the application context
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
