package org.springframework.beans;

import groovy.transform.builder.Builder;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.util.Arrays;

/**
 * 装载过程 demo
 *
 * @author haisen /20228/16
 */
public class BeanDefinitionReaderTest {

    public static void main(String[] args) {
        /*
         * 1、BeanDefinitionReader 读取 spring.xml
         * 2、读取后创建 BeanDefinition
         * 3、创建好后注册到 BeanDefinitionRegister
         */
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();           // 创建一个简单注册器
        BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);            // 创建 bean 定义读取器
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();             // 创建资源读取器
        /*
         * <bean id="student" name="student_aliase" class="org.springframework.beans.BeanDefinitionReaderTest.Student">
         *     <property name="id" value="1"></property>
         *     <property name="name" value="haisen"></property>
         * </bean>
         */
        Resource resource = resourceLoader.getResource("spring.xml");                   // 获取资源
        reader.loadBeanDefinitions(resource);                                           // TODO：装载类定义

        /*
         * 打印构建 Bean 名称：
         * registry.getBeanDefinitionNames()：
         *      // 如果未给 bean 设置 id 则：class+#+索引，来标识 id
         *      0 = "org.springframework.beans.BeanDefinitionReaderTest.Student#0"
         *      1 = "org.springframework.beans.BeanDefinitionReaderTest.Student#1"
         *
         *      // 如果 bean 设置了 id <bean id="student" class=...
         *      2 = "student"
         */
        System.out.println(Arrays.toString(registry.getBeanDefinitionNames()));     // [student]

        // <bean id="di" name="di2"
        System.out.println(registry.getBeanDefinition("student"));
        registry.registerAlias("student", "动态别名");
        /*
         * regitry.getAliases("student") = {String[2]@2562} ["student_aliase", "动态别名"]
         *      0 = "student_aliase"
         *      1 = "动态别名"
         */
        System.out.println(registry.getAliases("student"));
    }

    static class Student {
        private Integer id;
        private String name;

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
    }
}
