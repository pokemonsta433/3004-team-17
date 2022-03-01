package com.team17.quest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Game {
    Stack<Card> deck;
    ArrayList<Player> players;
    ArrayList<Card> discardPile;

    public Game(ArrayList<Player> players){ playGame(players); }

    public void generateDeck(){
        deck = new Stack<>();
        for(int i = 0; i < 99; i++){
            deck.add(new Card("card"));
        }
    }

    public void shuffleDeck(){ Collections.shuffle(deck); }

    public void createPlayers(ArrayList<Player> ps){
        players = ps;
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

    public void playGame(ArrayList<Player> players){
        //one time game set up
        generateDeck();
        shuffleDeck();
        createPlayers(players);
        dealHands();
        //game begins
        gameStart();
    }
}
