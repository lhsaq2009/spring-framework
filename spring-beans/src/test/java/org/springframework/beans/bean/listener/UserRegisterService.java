package org.springframework.beans.bean.listener;

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

import java.util.Date;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20229/4
 */
@Service
@EnableTransactionManagement
public class UserRegisterService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    public void publishEventWithTransactional(String username) throws InterruptedException {
        publisher.publishEvent(new UserRegisterEvent(publisher, username));
        Thread.sleep(10000);
        System.out.println("publishEvent " + new Date());
        // throw new RuntimeException();       // 如果取消注释，造成本事务失败，那么 toSendEmail() 也就不执行了。
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userRegisterWithTransactional(UserRegisterEvent event) throws InterruptedException {
        System.out.println("方式五：事务事件：" + event.username);
    }

    @Bean
    public TransactionManager transactionManager() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://rm-bp150s48920cjevsweo.mysql.rds.aliyuncs.com:3306/test");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("haisen");
        dataSource.setPassword("Haisen123");
        return new DataSourceTransactionManager(dataSource);
    }
}
