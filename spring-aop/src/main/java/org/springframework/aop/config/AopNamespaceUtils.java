package org.springframework.aop.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.lang.Nullable;

/**
 * Utility class for handling registration of auto-proxy creators used internally
 * by the '{@code aop}' namespace tags.
 *
 * <p>Only a single auto-proxy creator should be registered and multiple configuration
 * elements may wish to register different concrete implementations. As such this class
 * delegates to {@link AopConfigUtils} which provides a simple escalation protocol.
 * Callers may request a particular auto-proxy creator and know that creator,
 * <i>or a more capable variant thereof</i>, will be registered as a post-processor.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @since 2.0
 * @see AopConfigUtils
 */
public abstract class AopNamespaceUtils {

	/**
	 * The {@code proxy-target-class} attribute as found on AOP-related XML tags.
	 * true	：Spring 将创建子类来代理，false：基于接口的代理，默认 false
	 */
	public static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";

	/**
	 * The {@code expose-proxy} attribute as found on AOP-related XML tags.
	 * 示例：https://img2020.cnblogs.com/blog/2172049/202010/2172049-20201030173510067-846293690.png
	 * 解决内部 this 调用本类方法，也能走代理。默认 false
	 */
	private static final String EXPOSE_PROXY_ATTRIBUTE = "expose-proxy";

	/**
	 * 何种情况需要注册：InfrastructureAdvisorAutoProxyCreator.class
	 * =>> {@link AopConfigUtils#registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry, Object)}
	 */
	public static void registerAutoProxyCreatorIfNecessary(
			ParserContext parserContext, Element sourceElement) {

		BeanDefinition beanDefinition = AopConfigUtils.registerAutoProxyCreatorIfNecessary(		// 何种情况需要注册：<**:annotation-driven ...> =>>
				// parserContext.getRegistry() = {DefaultListableBeanFactory@3641} "DefaultListableBeanFactory@48af2d5d"
				parserContext.getRegistry(), parserContext.extractSource(sourceElement));
		useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
		registerComponentIfNecessary(beanDefinition, parserContext);
	}

	// 01、解析：<aop:config>，cls = AspectJAwareAdvisorAutoProxyCreator
	public static void registerAspectJAutoProxyCreatorIfNecessary(
			ParserContext parserContext, Element sourceElement) {
		// beanDefinition = {RootBeanDefinition@2657} "AspectJAwareAdvisorAutoProxyCreator"
		BeanDefinition beanDefinition = AopConfigUtils.registerAspectJAutoProxyCreatorIfNecessary(
				parserContext.getRegistry(), parserContext.extractSource(sourceElement));
		/*
		 * 判断 <aop:config> 是否设置：proxy-target-class 和 expose-proxy，并将标志设置进 beanDefinition
		 *
		 * beanDefinition = {RootBeanDefinition@2196} "Root bean: class [AspectJAwareAdvisorAutoProxyCreator] ..."
		 *      ...
		 *      propertyValues = {MutablePropertyValues@2213}
		 *          propertyValueList = {ArrayList@2222}  size = 2
		 *              0 = {PropertyValue@2244} "bean property 'order'"
		 *              1 = {PropertyValue@2245} "bean property 'proxyTargetClass'"
		 *
		 * parserContext.getRegistry() = {DefaultListableBeanFactory@2204}
		 * sourceElement = {DeferredElementNSImpl@2188} "[aop:config: null]"
		 */
		useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
		registerComponentIfNecessary(beanDefinition, parserContext);
	}

	// 02、解析 <aop:aspectj-autoproxy>，cls = AnnotationAwareAspectJAutoProxyCreator
	public static void registerAspectJAnnotationAutoProxyCreatorIfNecessary(
			ParserContext parserContext, Element sourceElement) {

		BeanDefinition beanDefinition = AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(	// 03、Sz^Rbp$g2#vU
				parserContext.getRegistry(), parserContext.extractSource(sourceElement));
		useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
		registerComponentIfNecessary(beanDefinition, parserContext);
	}

	// sourceElement = {DeferredElementNSImpl@2178} "[aop:aspectj-autoproxy: null]"
	private static void useClassProxyingIfNecessary(BeanDefinitionRegistry registry, @Nullable Element sourceElement) {
		if (sourceElement != null) {
			// eg：proxy-target-class="false"
			boolean proxyTargetClass = Boolean.parseBoolean(sourceElement.getAttribute(PROXY_TARGET_CLASS_ATTRIBUTE));
			if (proxyTargetClass) {
				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
			}
			// eg：expose-proxy = false
			boolean exposeProxy = Boolean.parseBoolean(sourceElement.getAttribute(EXPOSE_PROXY_ATTRIBUTE));
			if (exposeProxy) {
				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
			}
		}
	}

	private static void registerComponentIfNecessary(@Nullable BeanDefinition beanDefinition, ParserContext parserContext) {
		if (beanDefinition != null) {
			parserContext.registerComponent(
					new BeanComponentDefinition(beanDefinition, AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME));
		}
	}

}
