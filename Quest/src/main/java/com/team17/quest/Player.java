package com.team17.quest;

import java.util.ArrayList;

public class Player {
    String name;
    ArrayList<Card> hand;
    int shields;

    public Player(String n){
        name = n;
        shields = 0;
        hand = new ArrayList<>();
    }

    public Card discardCard(Card c){
        hand.remove(c);
        return c;
    }

    public ArrayList<Card> drawCard(ArrayList<Card> d){
        hand.add(d.remove(d.size() - 1));
        return d;
    }

    public ArrayList<Card> getHand(){
        return hand;
    }



}
