package org.springframework.beans.bean.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

// @EnableAspectJAutoProxy
@Configuration
public class aopConfig {

    @Bean
    public String haisenName() {
        return "haisen";
    }
}
