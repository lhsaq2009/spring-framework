package org.springframework.beans.bean.listener;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;

import static org.springframework.beans.bean.TaskExecuteConfig.POOL_TASK;

// 必须这个名字：AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster"
@Component("applicationEventMulticaster")
public class MyApplicationEventMulticaster
        extends SimpleApplicationEventMulticaster implements InitializingBean {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        // setTaskExecutor(threadPoolTaskExecutor);
        // setTaskExecutor(new SimpleAsyncTaskExecutor()); 也不行的
        // TODO：new SimpleAsyncTaskExecutor();
    }
}
