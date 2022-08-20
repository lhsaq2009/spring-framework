package org.springframework.beans;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20228/17
 */
public class BeanFactoryTest {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();      // 注册中心
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);      // 读取器
        reader.setResourceLoader(new DefaultResourceLoader());                          // 设置资源加载器
        reader.loadBeanDefinitions("spring.xml");                                       // 装载构建 Bean 的定义

        /*
         * 当 getBean 时，看下堆栈信息，如下：
         *
         *      instantiateClass:142,    BeanUtils (org.springframework.beans)
         *      instantiate:89,          SimpleInstantiationStrategy (org.springframework.beans.factory.support)

         *      instantiateBean:1147,    AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
         *      createBeanInstance:1099, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
         *      doCreateBean:513,        AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
         *      createBean:483,          AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
         *
         *      getObject:306,           AbstractBeanFactory$1 (org.springframework.beans.factory.support)  -- 匿名函数
         *      getSingleton:230,        DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
         *      doGetBean:302,           AbstractBeanFactory (org.springframework.beans.factory.support)
         *      getBean:197,             AbstractBeanFactory (org.springframework.beans.factory.support)
         *      main:25,                 BeanFactoryTest (com.demo.spring)
         */

//        System.out.println(beanFactory.getBean("student"));
//        System.out.println(beanFactory.getBean(BeanDefinitionReaderTest.Student.class));    // TODO：
        System.out.println(beanFactory.getBean("student_aliase"));
    }
}
