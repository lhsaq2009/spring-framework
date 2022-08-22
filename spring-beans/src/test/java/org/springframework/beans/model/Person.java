package org.springframework.beans.model;

public class Person implements IPerson {
    private String sex;

    public Person() {
    }

    public Person(String sex) {
        this.sex = sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
