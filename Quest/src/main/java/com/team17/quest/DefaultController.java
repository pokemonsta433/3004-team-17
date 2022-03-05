package com.team17.quest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

// this will be automatically pulled in by the application!

@Controller
@ControllerAdvice
public class DefaultController {

    ArrayList<Player> players = new ArrayList<>();
    int MAX_PLAYERS = 4;
    private SimpMessageSendingOperations messaging;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ServerMessage greeting(ClientMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new ServerMessage("Server message received from: " + HtmlUtils.htmlEscape(message.getName()));
    }

    @GetMapping(value = "/testmessage")
    public String testing(Model model) {
        System.out.println("===== testmessage");

        return "testmessage";
    }
    @GetMapping(value = "/")
    public String index(){
        return "redirect:/join";
        //return "testmessage";
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
        model.addAttribute("player", p);
        //messaging.convertAndSend("Player: " + p.getName() + " joined the game!");
        return "lobby";
    }

    @PostMapping(value = "game")
    public String start(@RequestParam(name = "playername") String playername, Model model){
        model.addAttribute("game", new Game(players));
        System.out.println("===== playername=" + playername);
/*        Player myPlayer = new Player();
        ArrayList<Player> otherplayers = new ArrayList<Player>();
        for(Player play : players){
            if(playername.equals(play.name)){
                myPlayer = play;
            }
            else
            {
                otherplayers.add(play);
            }
        }
        otherplayers.add(0, myPlayer);*/
        return "GameBoard";
    }

    @GetMapping(value = "/templates/Quest-Styles.css")
    public String CSS(Model model){
        model.addAttribute("css", "True");
        return "Quest-Styles.css";
    }

    @GetMapping(value = "/card")
    public ResponseEntity<InputStreamResource> getCard(@RequestParam(name= "cardName") String cardName) throws IOException {

        var imgFile = new ClassPathResource("assets/QuestsCards/AllCards/" + cardName + ".png");

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(imgFile.getInputStream()));
    }
}
