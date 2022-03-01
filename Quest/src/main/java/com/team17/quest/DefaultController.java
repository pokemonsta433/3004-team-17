package com.team17.quest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Objects;

// this will be automatically pulled in by the application!

@Controller
@ControllerAdvice
public class DefaultController {

    ArrayList<Player> players = new ArrayList<>();
    int MAX_PLAYERS = 4;

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
        if(Objects.equals(p.name, "")){
            return "redirect:/join";
        }
        if(players.size() >= MAX_PLAYERS){
            return "redirect:/join";
        }
        for(Player play : players){
            if(p.name.equals(play.name)){
                return "redirect:/join";
            }
        }
        players.add(p);
        model.addAttribute("PlayerList", players);
        return "lobby";
    }

    @GetMapping(value = "/templates/Quest-Styles.css")
    public String CSS(Model model){
        model.addAttribute("css", "True");
        return "Quest-Styles.css";
    }

    @GetMapping("/card") //This line tells the app to map this function to our website (http://localhost:8080)'s /hello GET request.
    //Click the little world icon next to "hello" and click "Generate request" to visit the site and try it yourself
    public String hello(@RequestParam(value = "cardName", defaultValue = "defaultCard") String cardName){
        return String.format("I'm giving you %s!", cardName); //functions can go in here, for the application.
    }
}