package org.springframework.beans.bean;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

// @Configuration
public class JavaConfig {

    @Bean(name = "messageSource")
    public MessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // 依旧中文乱码，检查 message.properties 的文件编码
        messageSource.setDefaultEncoding("UTF-8");
        // messageSource.setCacheSeconds(10);

        /** {@link org.springframework.context.support.AbstractResourceBasedMessageSource#basenameSet} */
        messageSource.addBasenames(
                "message",                  // resources/message.properties
                "message_en"                // resources/message_en.properties
        );
        return messageSource;
    }
}