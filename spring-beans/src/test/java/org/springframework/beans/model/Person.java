package org.springframework.beans.model;

import lombok.Builder;
import lombok.Data;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20228/22
 */
@Builder
@Data
public class Person implements IPerson {
    private String sex;

    public Person() {
    }

    public Person(String sex) {
        this.sex = sex;
    }
}
