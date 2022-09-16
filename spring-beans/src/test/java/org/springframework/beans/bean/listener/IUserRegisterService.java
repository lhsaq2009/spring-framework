package org.springframework.beans.bean.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20229/4
 */

public interface IUserRegisterService {
    public void publishEventWithTransactional(String username);
}
