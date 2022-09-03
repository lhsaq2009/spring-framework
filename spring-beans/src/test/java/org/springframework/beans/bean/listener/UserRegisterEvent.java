package org.springframework.beans.bean.listener;

import org.springframework.context.ApplicationEvent;

public class UserRegisterEvent extends ApplicationEvent {

    private static final long serialVersionUID = 5366526231219883438L;
    public String username;

    public UserRegisterEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}