package com.team17.quest;

import java.util.ArrayList;
import java.util.List;
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
        //this function is currently irrelevant and will be reworked in the future.
        for(Card c: hand){
            if(c.id == id){
                played.playcard(c);
                hand.remove(c);
            }
        }
    }

    public boolean validPlay(List<String> ids){
        boolean foe = false;
        ArrayList<String> weaponNames = new ArrayList<>();
        for(Card c : hand){
            if(ids.contains(Integer.toString(c.id))){
                if(c instanceof FoeCard) {
                    if (foe) {
                        return false;
                    } else {
                        foe = true;
                    }
                }
                else if(c instanceof WeaponCard){
                    if(weaponNames.contains(c.name)){
                        return false;
                    }
                    else{
                        weaponNames.add(c.name);
                    }
                }
            }
        }
        if(foe == false){
            return false;
        }
        else{
            return true;
        }
    }



    public void drawCard(Stack<Card> d, int n){
        for(int i = 0; i < n; i++){
            hand.add(d.pop());
        }
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
