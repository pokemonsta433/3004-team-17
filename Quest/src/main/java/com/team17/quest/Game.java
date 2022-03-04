package com.team17.quest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Game {
    Stack<Card> deck;
    ArrayList<Player> players;
    ArrayList<Card> discardPile;
    DeckFactory deckMaker;

    public Game(ArrayList<Player> ps){
        players = ps;
        deck = new Stack<>();
        discardPile = new ArrayList<>();
        deckMaker = new DeckFactory();
        playGame();
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }
    public Player getPlayer(int i){
        return players.get(i);
    }
    public void generateDeck(){
        deck = deckMaker.build("Adventure");
    }

    public void shuffleDeck(){ Collections.shuffle(deck); }


    public void dealHands(){
        for(Player player: players){
            player.hand.clear();
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

    public void playGame(){
        //one time game set up
        generateDeck();
        shuffleDeck();
        dealHands();
        //game begins
        gameStart();
    }
}
