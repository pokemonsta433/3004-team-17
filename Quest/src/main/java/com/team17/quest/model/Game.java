package com.team17.quest.model;

import com.team17.quest.model.storyDeck.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Game {
    public Stack<Card> adventure_deck;
    Stack<Card> story_deck;
    public ArrayList<Player> players;
    ArrayList<Card> adventure_discardPile;
    ArrayList<Card> story_discardPile;
    DeckFactory deckMaker;
    Card current_story;
    Player sponsor;
    int currentPlayerIndex;
    ArrayList<Player> questParticipants;
    int stages = 0;
    public ArrayList<ArrayList<AdventureCard>> quest;



    public Game(ArrayList<Player> ps){
        players = ps;
        adventure_deck = new Stack<>();
        story_deck = new Stack<>();
        adventure_discardPile = new ArrayList<>();
        story_discardPile = new ArrayList<>();
        deckMaker = new DeckFactory();
        quest = new ArrayList<>();
        playGame();
    }

    public int getIndexOfName(String n){
        int i = 0;
        for(Player p: players){
            if(p.name.equals(n)){
                return i;
            }
            i++;
        }
        return -1;
    }


    public Player getSponsor() {
        return sponsor;
    }


    public void setSponsor(String name){
        sponsor = getPlayer(getIndexOfName(name));
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public Player getPlayer(int i){
        return players.get(i);
    }

    public int getCurrentPlayer() {
        return this.currentPlayerIndex;
    }

    public void setCurrentPlayer(int playerIndex) {
        this.currentPlayerIndex = playerIndex;
    }

    public int getNextPlayer() {
        int nextPlayerIndex = (currentPlayerIndex + 1) % questParticipants.size();
        return nextPlayerIndex;
    }

    public int getStages() {
        return stages;
    }

    public void setStages(int stages) {
        this.stages = stages;
    }

    public ArrayList<ArrayList<AdventureCard>> getQuest() {
        return quest;
    }

    public void setQuest(ArrayList<ArrayList<AdventureCard>> quest) {
        this.quest = quest;
    }

    public Card getCurrent_story(){ return current_story;}

    public ArrayList<Card> getAdventure_discardPile() {
        return adventure_discardPile;
    }

    public Stack<Card> getAdventure_deck() {
        return adventure_deck;
    }

    public void generateDecks(){
        story_deck = deckMaker.build("Story");
        adventure_deck = deckMaker.build("Adventure");
    }

    public void shuffleDecks(){
        Collections.shuffle(adventure_deck);
        Collections.shuffle(story_deck);
    }

    public void dealHands(){
        for(Player player: players){
            player.hand.clear();
            for(int i = 0; i < 12; i++){
                player.hand.add(adventure_deck.pop());
            }
        }
    }

    public void addStage(Player p, List<String> ids){
        ArrayList<AdventureCard> stage = new ArrayList<>();
        for(Card c : p.hand){
            if(ids.contains(Integer.toString(c.id))){
                stage.add((AdventureCard) c);
            }
        }
        for(Card c : stage){
            p.hand.remove(c);
        }
        quest.add(stage);
    }

    public void drawStory(){
        quest.clear();
        if(current_story != null){
            story_discardPile.add(current_story);
        }
        current_story = story_deck.pop();
        if(current_story instanceof QuestCard){
            QuestCard card = (QuestCard) current_story;
            stages = card.stages;
        }
        else{
            stages = 0;
            if(current_story instanceof ChivalrousDeedEventCard) {
                current_story.eventStrategy(this);
            } else if(current_story instanceof CamelotEventCard) {
                current_story.eventStrategy(this);
            } else if(current_story instanceof KingsCallToArmsEventCard) {
                current_story.eventStrategy(this);
            } else if(current_story instanceof KingsRecognitionEventCard) {
                current_story.eventStrategy(this);
            } else if(current_story instanceof PlagueEventCard) {
                current_story.eventStrategy(this);
            }  else if(current_story instanceof PoxEventCard) {
                current_story.eventStrategy(this);
            } else if(current_story instanceof ProsperityEventCard) {
                current_story.eventStrategy(this);
            } else if(current_story instanceof QueensFavorEventCard) {
                current_story.eventStrategy(this);
            }
        }
    }

    public void discardStage(Player p){
        for(Card c : p.stage){
            adventure_discardPile.add(c);
        }
        p.stage.clear();
    }

    public void discardQuest(){
        for(ArrayList<AdventureCard> stage : quest){
            for(Card c : stage){
                adventure_discardPile.add(c);
            }
        }
        quest.clear();
    }
    public int getStageValue(int i){
        int total = 0;
        for(AdventureCard c: quest.get(i-1)){
            total += c.getValue(current_story.name);
        }
        return total;
    }
    public void playGame(){
        //one time game set up
        generateDecks();
        shuffleDecks();
        dealHands();
        //game begins

    }
}
