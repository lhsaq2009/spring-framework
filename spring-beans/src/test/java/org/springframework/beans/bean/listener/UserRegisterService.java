package org.springframework.beans.bean.listener;

import jodd.util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Date;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20229/4
 */
@Service
public class UserRegisterService implements IUserRegisterService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public void publishEventWithTransactional(String username) {

        System.out.println(TransactionAspectSupport.currentTransactionStatus());
        System.out.println(TransactionSynchronizationManager.isSynchronizationActive());
        System.out.println(TransactionSynchronizationManager.isActualTransactionActive());


        publisher.publishEvent(new UserRegisterEvent(publisher, username));
        // ThreadUtil.sleep(10_000);
        System.out.println("publishEvent " + new Date());
        // throw new RuntimeException();       // 如果取消注释，造成本事务失败，那么 toSendEmail() 也就不执行了。
    }

    // @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userRegisterWithTransactional(UserRegisterEvent event) throws InterruptedException {
        // System.out.println("方式五：事务事件：" + event.username);
    }
}
