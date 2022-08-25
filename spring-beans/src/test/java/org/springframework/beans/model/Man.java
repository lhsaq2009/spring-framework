package org.springframework.beans.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 生命周期的一些方法，{@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean(String, Object, RootBeanDefinition)}
 */
public class Man implements InitializingBean, BeanPostProcessor,
        MergedBeanDefinitionPostProcessor, BeanNameAware, ApplicationContextAware {
    private Women women;

    public Man() {
    }

    public Man(Women women) {
        this.women = women;
    }

    public Women getWomen() {
        return women;
    }

    public void setWomen(Women women) {
        this.women = women;
    }

    /**
     * {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#invokeCustomInitMethod}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Man implements InitializingBean --> afterPropertiesSet()");
    }

    /**
     * {@link org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#invokeCustomInitMethod}
     */
    public void init() {
        System.out.println("init() --> Man.init");
    }

    // 在调用 init-method() 之前执行
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization");
        return bean;
    }

    // 在调用 init-method() 之后执行
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization");
        return bean;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        System.out.println("postProcessMergedBeanDefinition from man");
    }

    @Override
    public void resetBeanDefinition(String beanName) {
        MergedBeanDefinitionPostProcessor.super.resetBeanDefinition(beanName);
        System.out.println("resetBeanDefinition from man");
    }

    @Override
    public void setBeanName(String name) {
        // TODO：???? 我继承好像没用？？
        // 会影响 IOC Bean 初始化时 Name 的设置吗，还是利用该接口，对外得到 Name 而已？？
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("spring init");
        // TODO：
        // context = applicationContext;
    }
}
