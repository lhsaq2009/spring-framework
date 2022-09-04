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
@EnableAsync
public class UserEmailAsyncService {

    // 异步执行，另起新线程执行，需要 AOP 创建代理类
    @Async
    // 问：若启用异步处理，这里需要数据库查询新注册用户信息，使用 @TransactionalEventListener 等待主逻辑事务结束再异步执行
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userRegisterAsync(UserRegisterEvent event) throws InterruptedException {
        Thread.sleep(30 * 1000);
        System.out.println("方式六：异步处理：" + event.username + "，" + new Date());
    }
}
