package org.springframework.beans;

import org.springframework.beans.bean.JavaConfig;
import org.springframework.beans.bean.listener.EmailServiceListener0;
import org.springframework.beans.bean.listener.UserRegisterEvent;
import org.springframework.beans.bean.listener.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


public class BeanFactoryTest {
    public static void main(String[] args) throws InterruptedException {

        // DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();      // 注册中心
        //
        // BeanDefinitionRegistry registry = beanFactory;
        // XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);         // 读取器
        // reader.setResourceLoader(new DefaultResourceLoader());                          // 设置资源加载器
        // reader.loadBeanDefinitions("spring.xml");                                       // 装载构建 Bean 的定义

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

        // AutowiredAnnotationBeanPostProcessor beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        // beanPostProcessor.setBeanFactory(beanFactory);
        // beanFactory.addBeanPostProcessor(beanPostProcessor);

        // CommonAnnotationBeanPostProcessor beanPostProcessor2 = new CommonAnnotationBeanPostProcessor();
        // beanPostProcessor2.setBeanFactory(beanFactory);
        // beanFactory.addBeanPostProcessor(beanPostProcessor2);

        // Object man = beanFactory.getBean("CustomerBean");
        // System.out.println(man);

        // TODO：BeanPostProcessor 未生效？？--> https://juejin.cn/post/6963641851047837704
        // https://www.jianshu.com/p/cb77412fde4d
        // 构建 ClassPathXmlApplicationContext 实例对象的时候，其中 refresh() 会调用 registerBeanPostProcessors()。
        // 这个方法会将检测到的 BeanPostProcessor 注入到 ClassPathXmlApplicationContext 容器中

        // ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        // Object person = context.getBean("CustomerBean");
        // System.out.println(person);

        String basePackages = "org.springframework.beans.bean";
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(basePackages), ctx = context;
        EmailServiceListener0.addApplicationListener(context);
        // context.register(JavaConfig.class);

        // 演示事务事件
        UserRegisterService userRegisterService = context.getBean(UserRegisterService.class);
        new Thread(() -> {      // 防止吧
            System.out.println("发布事务事件开始" + new Date());
            try {
                userRegisterService.publishEventWithTransactional("Haisen");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("发布事务事件结束" + new Date());
        }).start();

        // Thread.sleep(10000);
    }

    public static void messageSourceDemo() {
        // AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        // ctx.register(JavaConfig.class);
        // ctx.refresh();
        //
        // MessageSource ms = ctx.getBean(MessageSource.class);
        // System.out.println("zhMessage = " + ms.getMessage("user.name", null, null, null));
        // System.out.println("enMessage = " + ms.getMessage("user.sex", null, null, Locale.ENGLISH));
        // System.out.println("enMessage = " + ms.getMessage("user.sex", null, null, null));
    }
}