package org.springframework.beans.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.inject.Inject;

public class Customer {
    @Autowired
    // @Resource
    private Person person;

    private int type;
    private String action;

    /*@Autowired
    public Customer(Person person) {
        this.person = person;
    }*/

    @Autowired
    public void setPerson(Person person) {
        this.person = person;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Person getPerson() {
        return person;
    }

    public int getType() {
        return type;
    }

    public String getAction() {
        return action;
    }
}