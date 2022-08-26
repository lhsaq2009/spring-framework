package org.springframework.beans;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.model.IPerson;
import org.springframework.beans.model.Person;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20228/17
 */
public class BeanFactoryTest {
    public static void main(String[] args) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();      // 注册中心

        BeanDefinitionRegistry registry = beanFactory;
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);         // 读取器
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

        // beanFactory.registerSingleton("student2", new Student());                       // 手动注册

        /** {@link org.springframework.beans.factory.support.AbstractBeanFactory#beanPostProcessors} */
        // beanFactory.addBeanPostProcessor(new Man());

        Object man = beanFactory.getBean("man");
        System.out.println(man);

        // TODO：BeanPostProcessor 未生效？？--> https://juejin.cn/post/6963641851047837704
        // https://www.jianshu.com/p/cb77412fde4d
        // 构建 ClassPathXmlApplicationContext 实例对象的时候， 其中 refresh 方法会调用 registerBeanPostProcessors() 方法。这个方法会将检测到的 BeanPostProcessor 注入到 ClassPathXmlApplicationContext 容器中。

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");  // TODO：构造函数
//        Object person = context.getBean("person");
//        System.out.println(person);
    }

/**
 *                   HttpSession    Session
 *                       ▲ ▲           ▲
 *           ┌───────────┘ └────┐ ┌────┘
 * StandardSessionFacade  StandardSession
 */

}
