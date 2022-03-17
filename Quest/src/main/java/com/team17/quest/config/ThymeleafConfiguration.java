package com.team17.quest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;

@Configuration
public class ThymeleafConfiguration {
    public ThymeleafConfiguration() {

    }
    @Autowired
    private ServletContext context;
    // @Autowired
    // ServletContext context;

    // @Bean
    // @Description("Thymeleaf Template Resolver")
    // public ServletContextTemplateResolver templateResolver() {
    //     ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
    //     templateResolver.setPrefix("/WEB-INF/views/");
    //     templateResolver.setSuffix(".html");
    //     templateResolver.setTemplateMode("HTML5");

    //     return templateResolver;
    // }

    // @Bean
    // @Description("Thymeleaf Template Engine")
    // public SpringTemplateEngine templateEngine() {
    //     SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    //     templateEngine.setTemplateResolver(templateResolver());
    //     templateEngine.setTemplateEngineMessageSource(messageSource());
    //     return templateEngine;
    // }

    // @Bean
    // @Description("Thymeleaf View Resolver")
    // public ThymeleafViewResolver viewResolver() {
    //     ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    //     viewResolver.setTemplateEngine(templateEngine());
    //     viewResolver.setOrder(1);
    //     return viewResolver;
    // }

    // @Bean
    // @Description("Spring Message Resolver")
    // public ResourceBundleMessageSource messageSource() {
    //     ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    //     messageSource.setBasename("messages");
    //     return messageSource;
    // }
}
