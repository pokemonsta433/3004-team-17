package com.team17.quest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Game {
    Stack<Card> adventure_deck;
    Stack<Card> story_deck;
    ArrayList<Player> players;
    ArrayList<Card> adventure_discardPile;
    ArrayList<Card> story_discardPile;
    DeckFactory deckMaker;
    Card current_story;
    int player_turn;
    Player sponsor;
    ArrayList<Player> questParticipants;
    int stages = 0;


    public Game(ArrayList<Player> ps){
        players = ps;
        adventure_deck = new Stack<>();
        story_deck = new Stack<>();
        adventure_discardPile = new ArrayList<>();
        story_discardPile = new ArrayList<>();
        deckMaker = new DeckFactory();
        player_turn = 0;
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

    public void setSponsorAndParticipants(String name, ArrayList<String> pNames){
        questParticipants.clear();
        for(String n : pNames){
            questParticipants.add(getPlayer(getIndexOfName(n)));
        }
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

    public Card getCurrent_story(){ return current_story;}
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


    public void drawStory(){
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
        }
    }

    public void playGame(){
        //one time game set up
        generateDecks();
        shuffleDecks();
        dealHands();
        //game begins

    }
}
