package org.springframework.beans.bean.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceListener0 implements Ordered {

    // 方式一：通过 @EventListener 来监听，届时被收集的 Listerer 是当前类 EmailServiceListener0 哦
    @EventListener(classes = UserRegisterEvent.class)
    public void emailServiceListener1(UserRegisterEvent event) {
        System.out.println("使用注解的方式，收到事件：" + event.username);
    }

    @Override
    public int getOrder() {                 // 多个监听器按照指定顺序执行，可以通过实现 Ordered 接口，指定其顺序。
        return 10;                          // 数值越小，越优先执行
    }

    // 方式二：通过实现 ApplicationListener<?> 来监听
    @Bean
    public ApplicationListener<UserRegisterEvent> emailServiceListener2() {
        return new ApplicationListener<UserRegisterEvent>() {
            @Override
            public void onApplicationEvent(UserRegisterEvent event) {
                System.out.println("收到消息：" + event.username);
            }
        };
    }

    // 方式三：容器启动后，手动注册
    public static void addApplicationListener(ConfigurableApplicationContext context) {
        context.addApplicationListener(new ApplicationListener<UserRegisterEvent>() {
            @Override
            public void onApplicationEvent(UserRegisterEvent event) {
                System.out.println("context.addApplicationListener(...)：" + event.username);
            }
        });
    }
}