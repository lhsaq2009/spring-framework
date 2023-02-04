package org.springframework.aop.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Utility class for handling registration of AOP auto-proxy creators.
 *
 * <p>Only a single auto-proxy creator should be registered yet multiple concrete
 * implementations are available. This class provides a simple escalation protocol,
 * allowing a caller to request a particular auto-proxy creator and know that creator,
 * <i>or a more capable variant thereof</i>, will be registered as a post-processor.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @since 2.5
 * @see AopNamespaceUtils
 */
public abstract class AopConfigUtils {

	/**
	 * 内部管理的自动代理 创建者的 bean 名称
	 * CASE 1：Bean 注册：{@link AopConfigUtils#registerOrEscalateApcAsRequired(Class, BeanDefinitionRegistry, Object)}
	 */
	public static final String AUTO_PROXY_CREATOR_BEAN_NAME =
			"org.springframework.aop.config.internalAutoProxyCreator";		// 可能对应 3种 不同的类，所以并非某个具体的类名

	/**
	 * 按升级顺序存储 auto proxy 创建器类
	 * Stores the auto proxy creator classes in escalation order.
	 */
	private static final List<Class<?>> APC_PRIORITY_LIST = new ArrayList<>(3);

	/**
	 * 定义顺序，起到优先级的作用，何时奏效：AopConfigUtils.registerOrEscalateApcAsRequired
	 * 本来扫描到：<aop:config/> 先注册了个 AspectJAwareAdvisorAutoProxyCreator 处理类
	 * 后来发现又同时启用了 @EnableAspectJAutoProxy 或 <aop:aspectj-autoproxy，那么 AopConfigUtils#AUTO_PROXY_CREATOR_BEAN_NAME
	 * 对应的 BeanClass 就会升级为 AnnotationAwareAspectJAutoProxyCreator
	 *
	 * beanName = "internalAutoProxyCreator"
	 */
	static {
		// Set up the escalation list...	设置升级列表 ...
		APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class);		// 优先级最低，<**:annotation-driven  .../>
		APC_PRIORITY_LIST.add(AspectJAwareAdvisorAutoProxyCreator.class);		// TODO: @Aspect | <aop:config ...>
		APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class);	// 优先级最高：<aop:aspectj-autoproxy .../>
	}

	// region InfrastructureAdvisorAutoProxyCreator

	/**
	 * 何种情况需要注册：InfrastructureAdvisorAutoProxyCreator.class
	 * =>> {@link AopConfigUtils#registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry, Object)}
	 */
	@Nullable
	public static BeanDefinition registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
		// InfrastructureAdvisorAutoProxyCreator
		return registerAutoProxyCreatorIfNecessary(registry, null);		// 何种情况需要注册：@EnableTransactionManagement & @EnableCaching，=>>
	}

	/**
	 * internalAutoProxyCreator = "InfrastructureAdvisorAutoProxyCreator"
	 *
	 * CASE 1-1：<cache:annotation-driven>
	 * =>> {@link org.springframework.cache.config.AnnotationDrivenCacheBeanDefinitionParser#parse}
	 * 	   =>> {@link org.springframework.aop.config.AopNamespaceUtils#registerAutoProxyCreatorIfNecessary}  相同
	 *
	 * CASE 1-2：@EnableTransactionManagement(proxyTargetClass = true)
	 * =>> {@link org.springframework.context.annotation.AutoProxyRegistrar#registerBeanDefinitions}
	 *     if (mode == AdviceMode.PROXY)
	 *
	 * -------------------------------------------------------------
	 *
	 * CASE 2-1：<tx:annotation-driven/>，2023-01-21：分析 @Transactional
	 * =>> {@link org.springframework.transaction.config.AnnotationDrivenBeanDefinitionParser#parse}
	 * 	   =>> {@link org.springframework.aop.config.AopNamespaceUtils#registerAutoProxyCreatorIfNecessary}  相同
	 *
	 * CASE 2-2：@EnableCaching
	 * =>> {@link org.springframework.context.annotation.AutoProxyRegistrar#registerBeanDefinitions}
	 *     if (mode == AdviceMode.PROXY)
	 */
	@Nullable
	public static BeanDefinition registerAutoProxyCreatorIfNecessary(
			BeanDefinitionRegistry registry, @Nullable Object source) {
		return registerOrEscalateApcAsRequired(InfrastructureAdvisorAutoProxyCreator.class, registry, source);
	}

	// endregion

	// region AspectJAwareAdvisorAutoProxyCreator 解析 <aop:config>

	/**
	 * 解析 <aop:config>
	 * internalAutoProxyCreator = "AspectJAwareAdvisorAutoProxyCreator"
	 *
	 * =>> {@link AopNamespaceHandler#init()}
	 * 	   =>> {@link ConfigBeanDefinitionParser#parse}
	 * 	       =>> {@link AopNamespaceUtils#registerAspectJAutoProxyCreatorIfNecessary}
	 * 	   	       =>> {@link AopConfigUtils#registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry, Object)}
	 */
	@Nullable
	public static BeanDefinition registerAspectJAutoProxyCreatorIfNecessary(
			BeanDefinitionRegistry registry, @Nullable Object source) {
		return registerOrEscalateApcAsRequired(AspectJAwareAdvisorAutoProxyCreator.class, registry, source);
	}

	// endregion

	// region AnnotationAwareAspectJAutoProxyCreator 解析：<aop:aspectj-autoproxy> + @EnableAspectJAutoProxy

	/**
	 * internalAutoProxyCreator -> "AnnotationAwareAspectJAutoProxyCreator"
	 * =>> AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(..)
	 *
	 *   CASE 1：<aop:aspectj-autoproxy>
	 *
	 * =>> {@link AopNamespaceHandler#init()}
	 *     =>> {@link org.springframework.aop.config.AspectJAutoProxyBeanDefinitionParser#parse}
	 *         =>> {@link AopNamespaceUtils#registerAspectJAnnotationAutoProxyCreatorIfNecessary}
	 */
	@Nullable
	public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(					// 04、Sz^Rbp$g2#vU
			BeanDefinitionRegistry registry, @Nullable Object source) {
		return registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);
	}

	/**
	 * @EnableAspectJAutoProxy
	 *
	 * =>> {@link org.springframework.context.annotation.ConfigurationClassPostProcessor#processConfigBeanDefinitions}
	 * 	   =>> {@link org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader#loadBeanDefinitions}
	 *         =>> {@link org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader#loadBeanDefinitionsForConfigurationClass}
	 *             =>> {@link org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader#loadBeanDefinitionsFromRegistrars}
	 *         		   =>> {@link org.springframework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry, org.springframework.beans.factory.support.BeanNameGenerator)}
	 *     	       		   =>> {@link org.springframework.context.annotation.AspectJAutoProxyRegistrar#registerBeanDefinitions}
	 */
	@Nullable
	public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
		// AnnotationAwareAspectJAutoProxyCreator
		return registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry, null);
	}

	// endregion

	// 忽略，无调用者
	@Nullable
	@Deprecated
	public static BeanDefinition registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
		// AspectJAwareAdvisorAutoProxyCreator
		return registerAspectJAutoProxyCreatorIfNecessary(registry, null);  // 忽略
	}

	public static void forceAutoProxyCreatorToUseClassProxying(BeanDefinitionRegistry registry) {
		if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
			BeanDefinition definition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
			definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
		}
	}

	public static void forceAutoProxyCreatorToExposeProxy(BeanDefinitionRegistry registry) {
		if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
			BeanDefinition definition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
			definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
		}
	}

	/**
	 * {@link AopConfigUtils#APC_PRIORITY_LIST}
	 * 		CASE 1：{@link AopConfigUtils#registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry, Object)}
	 * 				cls = InfrastructureAdvisorAutoProxyCreator.class		<tx:annotation-driven ... /> -- @Transactional
	 *																		<cache:annotation-driven>
	 *																		@EnableTransactionManagement
	 *																		@EnableCaching
	 *
	 * 		CASE 2：{@link AopConfigUtils#registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry, Object)}
	 * 				cls = AspectJAwareAdvisorAutoProxyCreator.class  		<aop:config>
	 *
	 * 		CASE 3：{@link AopConfigUtils#registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry, Object)}
	 * 				cls = AnnotationAwareAspectJAutoProxyCreator.class		@EnableAspectJAutoProxy
	 * 																		<aop:aspectj-autoproxy .../>
	 */
	@Nullable
	private static BeanDefinition registerOrEscalateApcAsRequired(		// Escalate：逐步升级
			Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source) {

		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");

		// 如果已注册，则看看当前注册是不是优先级更高 ( APC_PRIORITY_LIST.indexOf(clazz) ) ，若更高则替换
		if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
			BeanDefinition apcDefinition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
			if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
				int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
				int requiredPriority = findPriorityForClass(cls);
				if (currentPriority < requiredPriority) {
					apcDefinition.setBeanClassName(cls.getName());
				}
			}
			return null;
		}

		// 未注册
		RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
		beanDefinition.setSource(source);
		beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
		beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
		return beanDefinition;
	}

	private static int findPriorityForClass(Class<?> clazz) {
		return APC_PRIORITY_LIST.indexOf(clazz);
	}

	private static int findPriorityForClass(@Nullable String className) {
		for (int i = 0; i < APC_PRIORITY_LIST.size(); i++) {
			Class<?> clazz = APC_PRIORITY_LIST.get(i);
			if (clazz.getName().equals(className)) {
				return i;
			}
		}
		throw new IllegalArgumentException(
				"Class name [" + className + "] is not a known auto-proxy creator class");
	}

}
