package org.springframework.beans.model;

public class Student extends Person implements IStudent {
    private Integer id;
    private String name;

    private Teacher teacher;

    public Student() {
    }

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void destroyMethod() {
        System.out.println("Student destroyMethod() ...");
    }

    public void initMethod() {
        System.out.println("Student initMethod() ...");
    }
}
