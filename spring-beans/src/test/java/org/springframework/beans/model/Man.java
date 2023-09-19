package org.springframework.beans.model;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;


public class Man {

	private String beanName;
	private Women women;

	public Man() {
	}

	public void say() {
		System.out.println("Man -> say()");
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
}

