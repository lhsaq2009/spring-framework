package org.springframework.beans;

import org.springframework.beans.model.Customer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20228/27
 */
public class ApplicationContextTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring.xml"});
        Customer cust = (Customer) context.getBean("CustomerBean");
        System.out.println(cust);
    }
}