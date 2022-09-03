package org.springframework.beans.bean.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

/*
 * ApplicationEvent      EventListener
 *        ▲                    ▲
 *        └──────────────────┐ │
 *     Ordered     ApplicationListener<E>
 *        ▲                    ▲
 *        └──────────────────┐ │
 *            SmartApplicationListener
 *          GenericApplicationListener
 */
@Service
public class EmailServiceListener implements SmartApplicationListener {

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {         // 指定支持哪些类型的事件
        if (eventType == UserRegisterEvent.class) {
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {                                // 事件支持的目标类
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserRegisterEvent) {
            System.out.println("[onApplicationEvent][给用户" + ((UserRegisterEvent) event).username + " 发送邮件]");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}