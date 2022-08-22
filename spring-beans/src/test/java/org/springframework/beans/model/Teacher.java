package org.springframework.beans.model;

import org.springframework.beans.BeanDefinitionReaderTest;

public class Teacher {
    private Integer id;
    private String name;

    private Student student;

    public Teacher() {
    }

    public Teacher(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
