package com.team17.quest;

import java.util.ArrayList;
import java.util.Stack;

public class Player {
    String name;
    ArrayList<Card> hand;
    int shields;
    Stage played;
    ArrayList<Card> allies;

    public Player(){
        name = "";
        shields = 0;
        hand = new ArrayList<>();
        played = new Stage();
        allies = new ArrayList<>();
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


    public void playCard(int id){
        for(Card c: hand){
            if(c.id == id){
                if(played.playcard(c) == true){
                    hand.remove(c);
                }
            }
        }
    }

    public void returnCardFromPlayer(int id){
        if(id == played.foe.id){
            hand.add(played.returnFoe());
            return;
        }
        else{
            for(Card c: played.weapons){
                if(c.id == id){
                    hand.add(played.returnWeapon(id));
                    return;
                }
            }
        }

    }

    public void drawCard(Stack<Card> d){
        hand.add(d.pop());
    }

    public Card discardCard(int id){
        for(Card c: hand) {
            if (c.id == id) {
                hand.remove(c);
                return c;
            }
        }
        return null;
    }

    public ArrayList<Card> getHand(){
        return hand;
    }

    public Stage getPlayed(){
        return played;
    }

    public int getShields(){ return shields; }
}
