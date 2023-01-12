package org.springframework.transaction.event;

import java.lang.reflect.Method;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * {@link EventListenerFactory} implementation that handles {@link TransactionalEventListener}
 * annotated methods.
 *
 * @author Stephane Nicoll
 * @since 4.2
 */
public class TransactionalEventListenerFactory implements EventListenerFactory, Ordered {

    private int order = 50;


    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }


    /**
     * 被调方法，是否是所支持的监听器的类型，判断是否包含 @TransactionalEventListener，则说明其是一个事务事件监听器
     */
    @Override
    public boolean supportsMethod(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, TransactionalEventListener.class);
    }

	/**
	 * 为目标方法生成一个事务事件监听器，这里 ApplicationListenerMethodTransactionalAdapter 实现了 ApplicationEvent 接口
	 */
    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        return new ApplicationListenerMethodTransactionalAdapter(beanName, type, method);
    }

}
