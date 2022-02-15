package com.team17.quest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring5.SpringTemplateEngine;

// this will be automatically pulled in by the application!

@RestController
public class DefaultController {

    @GetMapping("/")
    public String index() {
        return "This is the app!";
    }

    @GetMapping("/hello") //This line tells the app to map this function to our website (http://localhost:8080)'s /hello GET request.
    //Click the little world icon next to "hello" and click "Generate request" to visit the site and try it yourself
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name){
        return String.format("Hello %s!", name); //functions can go in here, for the application.
    }
}
