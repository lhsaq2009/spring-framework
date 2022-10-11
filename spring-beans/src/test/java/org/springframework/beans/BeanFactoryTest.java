package org.springframework.beans;

import org.springframework.beans.bean.aop.ArithmeticCalculator;
import org.springframework.beans.bean.aop2.LtwBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


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

        // ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

        // Object person = ctx.getBean("CustomerBean");
        // System.out.println(person);
        /*
         * calculator = {$Proxy23@2942} "org.springframework.beans.bean.aop.ArithmeticCalculatorImpl@631e06ab"
         *      h = {JdkDynamicAopProxy@3163}
         */
        ArithmeticCalculator calculator = (ArithmeticCalculator) ctx.getBean("arithmeticCalculator");
        /**
         * =>> JdkDynamicAopProxy#invoke(Object, Method,Object[])
         */
        if (calculator != null) {
            System.out.println(calculator.add(1, 2));
        }

        // test_LoadTimeWeaving(ctx);

        // String basePackages = "org.springframework.beans.bean";
        // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(basePackages), ctx = context;
        // context.register(JavaConfig.class);

        // EmailServiceListener0.addApplicationListener(context);
        // context.register(JavaConfig.class);

        // 演示事务事件
        // IUserRegisterService userRegisterService = context.getBean(IUserRegisterService.class);
        // new Thread(() -> {      // 防止吧
        //     System.out.println("发布事务事件开始" + new Date());
        //     userRegisterService.publishEventWithTransactional("Haisen");
        //     System.out.println("发布事务事件结束" + new Date());
        // }).start();

        // Thread.sleep(10000);
    }

    private static void test_LoadTimeWeaving(ApplicationContext ctx) {
        LtwBean bean = ctx.getBean(LtwBean.class);
        bean.test();
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