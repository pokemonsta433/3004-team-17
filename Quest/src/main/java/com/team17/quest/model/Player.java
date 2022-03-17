package com.team17.quest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Player {
    String name;
    ArrayList<Card> hand;
    int shields;
    ArrayList<Card> allies;
    ArrayList<AdventureCard> stage;

    public Player(){
        name = "";
        shields = 0;
        hand = new ArrayList<>();
        allies = new ArrayList<>();
        stage = new ArrayList<>();
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

    public void addStage(List<String> ids){
        for(Card c : hand){
            if(ids.contains(Integer.toString(c.id))){
                stage.add((AdventureCard) c);
            }
        }
        for(Card c : stage){
            hand.remove(c);
        }
    }

    public void drawCard(Stack<Card> d, int n){
        for(int i = 0; i < n; i++){
            hand.add(d.pop());
        }
    }

    public int getStageValue(String q){
        int total = 0;
        for(AdventureCard c: stage){
            total += c.getValue(q);
        }
        return total;
    }

    public int foeCount(){
        int i = 0;
        for(Card c : hand){
            if(c instanceof FoeCard){
                i++;
            }
        }
        return i;
    }
    public ArrayList<Card> getHand(){
        return hand;
    }

    public int getShields(){ return shields; }
}