package org.springframework.beans.model;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 生命周期的一些方法，{@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean(String, Object, RootBeanDefinition)}
 */
public class Man_life_method implements IPerson, InitializingBean, BeanPostProcessor,
		MergedBeanDefinitionPostProcessor, BeanNameAware/*, ApplicationContextAware*/ {

	private String beanName;
	private Women women;

	public Man_life_method() {
	}

	public void say() {
		System.out.println("Man -> say()");
	}

	public Man_life_method(Women women) {
		this.women = women;
	}

	public Women getWomen() {
		return women;
	}

	public void setWomen(Women women) {
		this.women = women;
	}

	/**
	 * ==> {@link AbstractAutowireCapableBeanFactory#invokeInitMethods(String, Object, RootBeanDefinition)} =>> ((InitializingBean) bean).afterPropertiesSet();
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Man implements InitializingBean --> afterPropertiesSet()");
	}

	/**
	 * <bean id="man" class="org.springframework.beans.model.Man" init-method="init">
	 * ==> {@link AbstractAutowireCapableBeanFactory#invokeInitMethods(String, Object, RootBeanDefinition)} =>> invokeCustomInitMethod(beanName, bean, mbd);
	 * ==> {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#invokeCustomInitMethod}
	 */
	public void init() {
		System.out.println("init() --> Man.init");
	}

	// 在调用 init-method 之前执行
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessBeforeInitialization");
		return bean;
	}

	// 在调用 init-method 之后执行
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessAfterInitialization");
		return bean;
	}

	/**
	 * 作用：比如 @Autowired 注解的实现，就是通过 AutowiredAnnotationBeanPostProcessor，它会在 applyMergedBeanDefinitionPostProcessors()
	 * 调用 AutowiredAnnotationBeanPostProcessor#postProcessMergedBeanDefinition(RootBeanDefinition, Class, String) 收集需要进行注入的 字段 和 方法
	 * <p>
	 * ==> {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean}
	 * ==> {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#applyMergedBeanDefinitionPostProcessors}
	 */
	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {

	}

	@Override
	public void resetBeanDefinition(String beanName) {
		MergedBeanDefinitionPostProcessor.super.resetBeanDefinition(beanName);
		System.out.println("resetBeanDefinition from man");
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;                       // 回调传给我用
	}

	/*
	 * private ApplicationContext context;
	 *
	 * 这个应该在 Web 环境下使用：
	 * import org.springframework.context.ApplicationContext;
	 * import org.springframework.context.ApplicationContextAware;
	 *
	 * @Override
	 * public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	 *     this.context = applicationContext;          // 回调传给我用
	 * }
	 */
}

