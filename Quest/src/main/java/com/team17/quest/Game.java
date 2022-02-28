package com.team17.quest;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    ArrayList<Card> deck;
    ArrayList<Player> players;
    ArrayList<Card> discardPile;

    public Game(ArrayList<String> pNames){ playGame(pNames); }

    public void generateDeck(){
        deck = new ArrayList<>();
        for(int i = 0; i < 99; i++){
            deck.add(new Card("card"));
        }
    }

    public void shuffleDeck(){ Collections.shuffle(deck); }

    public void createPlayers(ArrayList<String> names){
        for(String name: names){
            players.add(new Player(name));
        }
    }

    public void dealHands(){
        for(Player player: players){
            for(int i = 0; i < 12; i++){
                player.hand.add(deck.remove(deck.size() - 1));
            }
        }
    }

    public int getPlayerInput(Player p){
        //get input from frontend, gives index of card to discard or -1 if end turn.
        return 0;
    }

    public void gameStart(){
        for(Player player: players){
            int input = getPlayerInput(player);
            if(input >= 0){
                player.discardCard(player.hand.get(input));
                player.drawCard(deck);
            }
        }
    }

    public void playGame(ArrayList<String> pNames){
        //one time game set up
        generateDeck();
        shuffleDeck();
        createPlayers(pNames);
        dealHands();
        //game begins
        gameStart();
    }
}
