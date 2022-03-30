package com.team17.quest.controller;

import com.team17.quest.message.ClientMessage;
import com.team17.quest.model.AdventureCard;
import com.team17.quest.model.Card;
import com.team17.quest.model.Game;
import com.team17.quest.model.Player;
import com.team17.quest.message.ServerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// this will be automatically pulled in by the application!

@Controller
@ControllerAdvice
public class DefaultController {

    ArrayList<Player> players = new ArrayList<>();
    int MAX_PLAYERS = 4;
    Game game;
    boolean game_started = false;
    int players_prompted = 0;
    int player_turn = 0;
    int current_stage = 1;
    boolean sponsored = false;
    ArrayList<String> participants = new ArrayList<>();
    ArrayList<Boolean> challenge_played = new ArrayList<>();
    @Autowired
    private SimpMessagingTemplate messageSender;

    private void broadcastMessage(String messagetype, String message) {
        messageSender.convertAndSend("/topic/serverMessages",
                new ServerMessage(messagetype, HtmlUtils.htmlEscape(message)));
    }
    public void questLogic(String n){
        game.discardStage(game.getPlayer(game.getIndexOfName(n)));
        if(challenge_played.contains(false)){
            messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(n)).getName(), "/reply", new ServerMessage("Quest", "Wait"));
        }
        else{
            if(current_stage == game.getStages() || participants.size() == 0){
                current_stage = 1;
                for(String s : participants){
                    game.getPlayer(game.getIndexOfName(s)).shields += game.getStages();
                }
                for(ArrayList<AdventureCard> stage : game.quest){
                    Card c = game.getSponsor().drawCard(game.adventure_deck);
                    for(Card x : stage){
                        c = game.getSponsor().drawCard(game.adventure_deck);
                    }
                }
                game.discardQuest();
                game.drawStory();
                //give sponsor correct # of cards
                for(Player p : players){
                    messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Update", "Next Quest"));
                }
                participants.clear();
                challenge_played.clear();
                sponsored = false;
                players_prompted = 0;
                player_turn = (player_turn + 2) % game.players.size();
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Sponsor")); //TO-DO change content to quest name?
            }
            else{
                current_stage++;
                challenge_played.clear();
                for(String s: participants){
                    //send via DOM
                    Card c = game.getPlayer((game.getIndexOfName(s))).drawCard(game.adventure_deck);
                }
                for(String s : participants){
                    challenge_played.add(false);
                    messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(s)).getName(), "/reply", new ServerMessage("Quest", "Continue"));
                }
            }
        }
    }

    @MessageMapping("/challenge")
    public void challenge(ClientMessage message) throws Exception{
        String[] split = message.getMsg().split(",");
        List<String> ids = Arrays.asList(split);
        boolean valid = game.getPlayer(game.getIndexOfName(message.getName())).validPlay(ids);
        if(!valid){
            messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(message.getName())).getName(), "/reply", new ServerMessage("Quest", "Invalid"));
        }
        else{
            game.getPlayer(game.getIndexOfName(message.getName())).addStage(ids);
            if(game.getPlayer(game.getIndexOfName(message.getName())).getStageValue(game.getCurrent_story().getName()) > game.getStageValue(current_stage)){
                challenge_played.set(participants.indexOf(message.getName()), true);
                questLogic(message.getName());
            }
            else{
                challenge_played.remove(participants.indexOf(message.getName()));
                participants.remove(message.getName());
                messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(message.getName())).getName(), "/reply", new ServerMessage("Quest", "Lose"));
                questLogic(message.getName());
            }
        }
    }
    @MessageMapping("/playCards")
    public void answer(ClientMessage message) throws Exception {
        String[] split = message.getMsg().split(",");
        List<String> ids = Arrays.asList(split);
        boolean valid = game.getPlayer(game.getIndexOfName(message.getName())).validPlay(ids);
        if(valid){
            game.addStage(game.getPlayer(game.getIndexOfName(message.getName())), ids);
            if(game.getQuest().size() < game.getStages()){
                messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(message.getName())).getName(), "/reply", new ServerMessage("Quest", "Next"));
            }
            else{
                messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(message.getName())).getName(), "/reply", new ServerMessage("Quest", "Complete"));
                for(String s: participants){
                    //send via DOM
                    Card c = game.getPlayer((game.getIndexOfName(s))).drawCard(game.adventure_deck);
                }
                for(String s : participants){
                    challenge_played.add(false);
                    messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(s)).getName(), "/reply", new ServerMessage("Quest", "Stage"));
                }
            }
        }
        else{
            messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(message.getName())).getName(), "/reply", new ServerMessage("Quest", "Invalid"));
        }
    }

    @MessageMapping("/prompt")
    public void prompt(ClientMessage message) throws Exception {
        if(message.getMsg().equals("Sponsor")){
            if(game.getPlayer(player_turn).foeCount() < game.getStages()){ //checks if has enough foes to make stage (later will change to foe + hasTest)
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "No Sponsor"));
                return;
            }
            else{
                sponsored = true;
                game.setSponsor(message.getName());
            }
        }
        else if(message.getMsg().equals("Participate")){
            participants.add(message.getName());
        }
        players_prompted += 1;
        if(players_prompted >= game.getPlayers().size()){
            players_prompted = 0;
            if(!sponsored || participants.size() == 0){
                game.drawStory();
                for(Player p : players){
                    messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Update", "Next Quest"));
                }
                participants.clear();
                challenge_played.clear();
                sponsored = false;
                players_prompted = 0;
                player_turn = (player_turn + 2) % game.getPlayers().size();
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Sponsor")); //TO-DO change content to quest name?
            }
            else{
                messageSender.convertAndSendToUser(game.getSponsor().getName(), "/reply", new ServerMessage("Quest", "First"));
            }
        }
        else{
            player_turn += 1;
            player_turn = player_turn % game.getPlayers().size();
            if(sponsored){
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "No Sponsor"));
            }
            else{
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Sponsor"));
            }
        }
    }

    @MessageMapping("/gameStart")
    public void gameStart(ClientMessage message) throws Exception {
        messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Sponsor")); //TO-DO change content to quest name?
    }

    @GetMapping(value = "/")
    public String index(){
        return "redirect:/join";
    }

    @GetMapping(value = "/join")
    public String Join(Model model) {
        model.addAttribute("Player", new Player());
        return "join";
    }

    @GetMapping(value = "/lobby")
    public String Lobby(Model model) {
        model.addAttribute("PlayerList", players);
        //model.addAttribute("player", p);
        return "lobby";
    }

    @PostMapping(value = "/lobby")
    public String Lobby(@ModelAttribute("Player") Player p, Model model) {
        if(Objects.equals(p.getName(), "")){
            return "redirect:/join";
        }
        if(players.size() >= MAX_PLAYERS){
            return "redirect:/join";
        }
        for(Player play : players){
            if(p.getName().equals(play.getName())){
                return "redirect:/join";
            }
        }
        players.add(p);
        model.addAttribute("PlayerList", players);
        model.addAttribute("player", p);
        broadcastMessage("Join", p.getName());
        return "lobby";
    }

    @PostMapping(value = "game")
    public String start(@RequestParam(name = "playername") String playername, Model model){
        if(!game_started){
            game = new Game(players);
            game_started = true;
        }
        model.addAttribute("game", game);
        model.addAttribute("i", game.getIndexOfName(playername));
        broadcastMessage("Start", "StartGame");
        game.drawStory();
        return "GameBoard";
    }

    @GetMapping(value = "gameboard")
    public String observe(@RequestParam(name = "playername") String playername, Model model){
        model.addAttribute("game", game);
        model.addAttribute("i", game.getIndexOfName(playername));
        return "GameBoard";
    }


    @GetMapping(value = "/templates/Global.css")
    public String globalCSS(Model model){
        model.addAttribute("css", "True");
        return "Global.css";
    }

    @GetMapping(value = "/templates/Gameboard.css")
    public String gameboardCSS(Model model){
        model.addAttribute("css", "True");
        return "Gameboard.css";
    }

    @GetMapping(value = "/card")
    public ResponseEntity<InputStreamResource> getCard(@RequestParam(name= "cardName") String cardName) throws IOException {

        var imgFile = new ClassPathResource("assets/QuestsCards/AllCards/" + cardName + ".png");

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(imgFile.getInputStream()));
    }
    @GetMapping(value = "/js")
    public String JavaScript(Model model){
        model.addAttribute("JavaScript", "True");
        return "Messaging.js";
    }

    @GetMapping(value = "/testmessage")
    public String testing(Model model) {
        System.out.println("===== testmessage");
        return "testmessage";
    }
}
