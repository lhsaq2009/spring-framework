package org.springframework.beans;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.model.Customer;
import org.springframework.beans.model.IPerson;
import org.springframework.beans.model.Man;
import org.springframework.beans.model.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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

        AutowiredAnnotationBeanPostProcessor beanPostProcessor =
                new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(beanPostProcessor);

//        CommonAnnotationBeanPostProcessor beanPostProcessor2 = new CommonAnnotationBeanPostProcessor();
//        beanPostProcessor2.setBeanFactory(beanFactory);
//        beanFactory.addBeanPostProcessor(beanPostProcessor2);

        Object man = beanFactory.getBean("CustomerBean");
        System.out.println(man);

        // TODO：BeanPostProcessor 未生效？？--> https://juejin.cn/post/6963641851047837704
        // https://www.jianshu.com/p/cb77412fde4d
        // 构建 ClassPathXmlApplicationContext 实例对象的时候， 其中 refresh() 会调用 registerBeanPostProcessors()。
        // 这个方法会将检测到的 BeanPostProcessor 注入到 ClassPathXmlApplicationContext 容器中

//        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");  // TODO：=>> 构造函数
//        Object person = context.getBean("CustomerBean");
//        System.out.println(person);
    }
}
