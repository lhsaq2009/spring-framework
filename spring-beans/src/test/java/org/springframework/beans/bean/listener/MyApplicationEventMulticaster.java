package org.springframework.beans.bean.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

// 必须这个名字：AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster"
@Component("applicationEventMulticaster")
public class MyApplicationEventMulticaster extends SimpleApplicationEventMulticaster {

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        super.addApplicationListener(listener);
    }

    @Override
    public void addApplicationListenerBean(String listenerBeanName) {
        super.addApplicationListenerBean(listenerBeanName);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        super.removeApplicationListener(listener);
    }

    @Override
    public void removeApplicationListenerBean(String listenerBeanName) {
        super.removeApplicationListenerBean(listenerBeanName);
    }

    @Override
    public void removeAllListeners() {
        super.removeAllListeners();
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        super.multicastEvent(event);
    }

    @Override
    public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
        super.multicastEvent(event, eventType);
    }
}
