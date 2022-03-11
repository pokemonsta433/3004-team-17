package com.team17.quest;

import java.util.ArrayList;
import java.util.Stack;

public class Player {
    String name;
    ArrayList<Card> hand;
    int shields;

    public Player(){
        name = "";
        shields = 0;
        hand = new ArrayList<>();
    }


    public Player(String n){
        name = n;
        shields = 0;
        hand = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public void setName(String n){
        name = n;
    }


    public Card discardCard(int id){
        for(Card c: hand){
            if(c.id == id){
                hand.remove(c);
                return c;
            }
        }
        return null;
    }

    public Card getById(int i){
        for(Card c: hand){
            if(c.id == i) return c;
        }
        return null;
    }

    public void drawCard(Stack<Card> d){
        hand.add(d.pop());
    }

    public ArrayList<Card> getHand(){
        return hand;
    }

    public int getShields(){ return shields; }
}
