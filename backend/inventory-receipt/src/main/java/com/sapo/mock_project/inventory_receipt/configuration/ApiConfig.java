package com.sapo.mock_project.inventory_receipt.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@EnableCaching
public class ApiConfig {
    @Value("${spring.messages.basename}")
    private String baseName;

    @Value("${spring.messages.encoding}")
    private String defaultEncoding;

    @Bean
    public Environment env(ConfigurableApplicationContext context) {
        return context.getEnvironment();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:" + baseName);
        messageSource.setDefaultEncoding(defaultEncoding);
        return messageSource;
    }
}
