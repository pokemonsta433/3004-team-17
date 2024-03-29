package com.team17.quest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class QuestApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuestApplication.class, args);
    }

    //uncomment to see the existing beans on startup
   /*
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("these are the existing beans:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames){
                System.out.println(beanName);
            }
        };
    }
    */



}
