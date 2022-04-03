package com.team17.quest.controller;

import com.team17.quest.message.ClientMessage;
import com.team17.quest.model.*;
import com.team17.quest.message.ServerMessage;
import com.team17.quest.model.storyDeck.EventCard;
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

    //some bidding globals <-- If we had more time these would be a part of the models
    int bids_recieved = 0;
    int largest_bid = 0;
    String best_bidder = "";


    @Autowired
    private SimpMessagingTemplate messageSender;

    private void broadcastMessage(String messagetype, String message) {
        messageSender.convertAndSend("/topic/serverMessages",
                new ServerMessage(messagetype, HtmlUtils.htmlEscape(message)));
    }

    public void drawNewStory(){
        game.drawStory();
        for (Player p : players) {
            messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Update", "Next Story"));
        }
        participants.clear();
        challenge_played.clear();
        sponsored = false;
        player_turn = (player_turn + 2) % game.getPlayers().size();
        if (game.getCurrent_story() instanceof QuestCard) {
            messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Sponsor"));
        } else if (game.getCurrent_story() instanceof TournamentCard) {
            messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Tournament"));
        } else if (game.getCurrent_story() instanceof EventCard) {
            String w = checkWin();
            if(w.length() > 0){
                for(Player p: players){
                    messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Win", w));
                }
                return;
            }
            for(Player p: players){
                messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Prompt", "EventCard"));
            }
        }
    }

    public void questLogic(String n){
        game.discardStage(game.getPlayer(game.getIndexOfName(n)));
        if(challenge_played.contains(false)){
            messageSender.convertAndSendToUser(n, "/reply", new ServerMessage("Quest", "Wait"));
        }
        else{
            if(current_stage == game.getStages() || participants.size() == 0){
                current_stage = 1;
                for(String s : participants){
                    game.getPlayer(game.getIndexOfName(s)).shields += game.getStages();
                }
                String w = checkWin();
                if(w.length() > 0){
                    for(Player p: players){
                        messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Win", w));
                    }
                    return;
                }
                for(ArrayList<AdventureCard> stage : game.quest){
                    game.getSponsor().drawCard(game.adventure_deck, 1);
                    for(Card x : stage){
                        game.getSponsor().drawCard(game.adventure_deck, 1);
                    }
                }
                game.discardQuest();
                for(Player p : game.players){
                    p.amours.clear();
                }
                players_prompted = 0;
                drawNewStory();
            }
            else{
                current_stage++;
                challenge_played.clear();
                for(String s: participants){
                    game.getPlayer((game.getIndexOfName(s))).drawCard(game.adventure_deck, 1);
                }
                for(Player p : players){
                    messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Update", "Next Quest"));
                }
                for(String s : participants){
                    challenge_played.add(false);
                    messageSender.convertAndSendToUser(s, "/reply", new ServerMessage("Quest", "Continue"));
                }
            }
        }
    }

    public String checkWin(){
        String w = "";
        for(Player p : players){
            if(p.shields >= 5){
                w += p.getName();
                w += " ";
            }
        }
        return w;
    }

    public void tournamentLogic(String n){
        if(challenge_played.contains(false)){
            messageSender.convertAndSendToUser(n, "/reply", new ServerMessage("Tournament", "Wait"));
        }
        else{
            int max = 0;
            for(String s : participants){
                if(game.getPlayer(game.getIndexOfName(s)).getStageValue(game.getCurrent_story().getName(), game.players) > max){
                    max = game.getPlayer(game.getIndexOfName(s)).getStageValue(game.getCurrent_story().getName(), game.players);
                }
            }
            for(String s : participants){
                if(game.getPlayer(game.getIndexOfName(s)).getStageValue(game.getCurrent_story().getName(), game.players) == max){
                    game.getPlayer(game.getIndexOfName(s)).shields += participants.size();
                    TournamentCard t = (TournamentCard) game.getCurrent_story();
                    game.getPlayer(game.getIndexOfName(s)).shields += t.bonus_shields;
                    String w = checkWin();
                    if(w.length() > 0){
                        for(Player p: players){
                            messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Win", w));
                        }
                        return;
                    }
                }
            }
            for(Player p : game.players){
                p.amours.clear();
            }
            players_prompted = 0;
            drawNewStory();
        }
    }

    @MessageMapping("/challenge")
    public void challenge(ClientMessage message) throws Exception{
        String[] split = message.getMsg().split(",");
        List<String> ids = Arrays.asList(split);
        game.getPlayer(game.getIndexOfName(message.getName())).addStage(ids);
        if(game.getPlayer(game.getIndexOfName(message.getName())).getStageValue(game.getCurrent_story().getName(), game.players) > game.getStageValue(current_stage)){
            challenge_played.set(participants.indexOf(message.getName()), true);
            questLogic(message.getName());
        }
        else{
            challenge_played.remove(participants.indexOf(message.getName()));
            participants.remove(message.getName());
            messageSender.convertAndSendToUser(message.getName(), "/reply", new ServerMessage("Quest", "Lose"));
            questLogic(message.getName());
        }
    }

    @MessageMapping("/tournament")
    public void tournament(ClientMessage message) throws Exception{
        String[] split = message.getMsg().split(",");
        List<String> ids = Arrays.asList(split);
        System.out.println(ids);
        game.getPlayer(game.getIndexOfName(message.getName())).addStage(ids);
        challenge_played.set(participants.indexOf(message.getName()), true);
        tournamentLogic(message.getName());
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
                for(String s: participants) {
                    game.getPlayer((game.getIndexOfName(s))).drawCard(game.adventure_deck, 1);
                }
                if(true) { // TODO: actually this should be if the upcoming stage is a quest
                    for (String s : participants) {
                        challenge_played.add(false);
                        messageSender.convertAndSendToUser(s, "/reply", new ServerMessage("Quest", "Stage"));
                    }
                }
                else{ //if we're looking at a test stage, let's send a message to the first bidder, asking for his bid!
                    //TODO: Check for minimum bid, because that's what we'll be sending as the second "param" after BidRequest
                    messageSender.convertAndSendToUser(participants.get(0), "/reply", new ServerMessage("Quest", "BidRequest " + 0));
                }
            }
        }
        else{
            messageSender.convertAndSendToUser(game.getPlayer(game.getIndexOfName(message.getName())).getName(), "/reply", new ServerMessage("Quest", "Invalid"));
        }
    }

    @MessageMapping("/bid")
    public void bid(ClientMessage message) throws Exception {
        bids_recieved += 1;
        String[] card_ids = message.getMsg().split(",");
        List<String> ids = Arrays.asList(card_ids);
        int currentBid = ids.size();
        if (currentBid > largest_bid) {
            largest_bid = currentBid;
            best_bidder = message.getName();
        }
        else messageSender.convertAndSendToUser(message.getName(), "/reply", new ServerMessage("Quest", "Bid Lost"));
        if (bids_recieved >= participants.size()) { //in a quest, the participants array persists
            bids_recieved = 0;
                for (String p : participants) {
                    if (p.equals(best_bidder))
                        messageSender.convertAndSendToUser(p, "/reply", new ServerMessage("Quest", "Bid Won"));
                    else
                        messageSender.convertAndSendToUser(p, "/reply", new ServerMessage("Quest", "Bid Lost"));
                }
            } else { //ask the next player for their own bid
                messageSender.convertAndSendToUser(participants.get(participants.indexOf(message.getName()) + 1), "/reply", new ServerMessage("Quest", "BidRequest " + largest_bid));
            }
        }

    @MessageMapping("/prompt")
    public void prompt(ClientMessage message) throws Exception {
        players_prompted += 1;
        if(message.getMsg().equals("EventCard")){
            if (players_prompted >= game.getPlayers().size()) {
                players_prompted = 0;
                player_turn += 1;
                drawNewStory();
            }
        }
        else if(message.getMsg().equals("Tournament") || message.getMsg().equals("DropTourney")) {
            if(message.getMsg().equals("Tournament")){
                participants.add(message.getName());
            }
            if (players_prompted >= game.getPlayers().size()) {
                players_prompted = 0;
                if (participants.size() == 0) {
                    drawNewStory();
                }
                else{
                    for(String s: participants){
                        game.getPlayer((game.getIndexOfName(s))).shields++;
                        game.getPlayer((game.getIndexOfName(s))).drawCard(game.adventure_deck, 1);
                        challenge_played.add(false);
                        messageSender.convertAndSendToUser(s, "/reply", new ServerMessage("Tournament", "Participate"));
                    }
                }
            }
            else{
                player_turn += 1;
                player_turn = player_turn % game.getPlayers().size();
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Tournament"));
            }
        }
        else{
            if((game.getPlayer(player_turn).foeCount() + game.getPlayer(player_turn).hasTest()) < game.getStages()){ //checks if has enough foes to make stage
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Foe Issue"));
            }
            else if(message.getMsg().equals("Sponsor")){
                sponsored = true;
                game.setSponsor(message.getName());
            }
            else if(message.getMsg().equals("Participate")){
                participants.add(message.getName());
            }
            if(players_prompted >= game.getPlayers().size()){
                players_prompted = 0;
                if(!sponsored || participants.size() == 0){
                    drawNewStory();
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
    }

    @MessageMapping("/gameStart")
    public void gameStart(ClientMessage message) throws Exception {
        if(game_started){
            if(game.getCurrent_story() instanceof QuestCard){
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Sponsor"));
            }
            else if(game.getCurrent_story() instanceof TournamentCard){
                messageSender.convertAndSendToUser(game.getPlayer(player_turn).getName(), "/reply", new ServerMessage("Prompt", "Tournament"));
            } else if (game.getCurrent_story() instanceof EventCard) {
                for(Player p: players){
                    messageSender.convertAndSendToUser(p.getName(), "/reply", new ServerMessage("Prompt", "EventCard"));
                }
            }
        }
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
        model.addAttribute("stage_number", current_stage);
        model.addAttribute("game", game);
        model.addAttribute("i", game.getIndexOfName(playername));
        broadcastMessage("Start", "StartGame");
        game.drawStory();
        return "GameBoard";
    }

    @GetMapping(value = "gameboard")
    public String observe(@RequestParam(name = "playername") String playername, Model model){
        model.addAttribute("game", game);
        model.addAttribute("stage_number", current_stage);
        model.addAttribute("i", game.getIndexOfName(playername));
        return "GameBoard";
    }

    @GetMapping(value = "gameOver")
    public String gameOver(Model model){
        String w = checkWin();
        model.addAttribute("winners", w);
        return "GameOver";
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
