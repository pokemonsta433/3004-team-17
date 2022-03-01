package com.team17.quest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

// this will be automatically pulled in by the application!

@Controller
public class DefaultController {

    @GetMapping(value = "/")
    public String index(Model model) {
        return Join(model);
    }

    @GetMapping(value = "/Join")
    @ResponseBody
    public String Join(Model model) {
        model.addAttribute("name", "Isaac");
        return "Join";
    }

    @GetMapping(value = "/Lobby")
    @ResponseBody
    public String Game(@RequestParam(value="name", defaultValue = "AnonymousPlayer") String name, Model model) {
        model.addAttribute("name", name);
        return "Lobby";
    }

    @GetMapping(value = "/templates/Quest-Styles.css")
    public String CSS(Model model){
        model.addAttribute("css", "True");
        return "Quest-Styles.css";
    }

    @GetMapping("/hello") //This line tells the app to map this function to our website (http://localhost:8080)'s /hello GET request.
    //Click the little world icon next to "hello" and click "Generate request" to visit the site and try it yourself
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name){
        return String.format("Hello %s!", name); //functions can go in here, for the application.
    }
}