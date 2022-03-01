package com.team17.quest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

// this will be automatically pulled in by the application!

@Controller
@ControllerAdvice
public class DefaultController {

    @GetMapping(value = "/")
    public String index(Model model){
        return "redirect:/join";
    }
    @GetMapping(value = "/join")
    public String Join(Model model) {
        model.addAttribute("Player", new Player());
        return "join";
    }

    @PostMapping(value = "/lobby")
    public String Lobby(@ModelAttribute("Player") Player p, Model model) {
        model.addAttribute("name", p.name);
        return "lobby";
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