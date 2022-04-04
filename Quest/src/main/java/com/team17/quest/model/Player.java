package com.team17.quest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Player {
    String name;
    ArrayList<Card> hand;
    public int shields;
    ArrayList<AllyCard> allies;
    public ArrayList<AmourCard> amours;
    public ArrayList<AdventureCard> stage;
    int rank;

    public Player(){
        name = "";
        shields = 0;
        hand = new ArrayList<>();
        allies = new ArrayList<>();
        amours = new ArrayList<>();
        stage = new ArrayList<>();
        rank = 0;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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
                else if(c instanceof TestCard){
                    return true; //for now we don't have to check for multiple tests due to front-end stopgaps
                }
            }
        }
        return foe;
    }

    public void addToBid(List<String> ids){
        for(Card c : hand){
            if(ids.contains(Integer.toString(c.id))){
                stage.add((AdventureCard) c);
            }
        }
        for(Card c : stage){
            hand.remove(c);
        }
    }

    public void returnToHand(){
        hand.addAll(stage);
        stage.clear();
    }

    public void addStage(List<String> ids){
        for(Card c : hand){
            if(ids.contains(Integer.toString(c.id))){
                if(c instanceof AllyCard){
                    allies.add((AllyCard) c);
                }
                else if(c instanceof AmourCard){
                    amours.add((AmourCard) c);
                }
                else {
                    stage.add((AdventureCard) c);
                }
            }
        }
        for(Card c : stage){
            hand.remove(c);
        }
        for(Card c : allies){
            if(hand.contains(c)){
                hand.remove(c);
            }
        }
        for(Card c : amours){
            if(hand.contains(c)){
                hand.remove(c);
            }
        }
    }

    public void drawCard(Stack<Card> d, int n){
        for(int i = 0; i < n; i++){
            if(hand.size() < 12) {
                hand.add(d.pop());
            }
        }
    }

    public Card drawCard(Stack<Card> d){
        if(hand.size() < 12){
            Card c = d.pop();
            hand.add(c);
            return c;
        }
        return null;
    }

    public int getStageValue(String q, ArrayList<Player> ps){
        int total = 5;
        for(AdventureCard c: stage){
            total += c.getValue(q);
        }
        for(AdventureCard c : allies){
            if(c.name.equals("sir_tristan")){ //loop through to see if queen iseult is in play
                for(Player p : ps){
                    for(Card a : p.allies){
                        if(a.name.equals("queen_iseult")){
                            total += 10;
                            break;
                        }
                    }
                }
            }
            total += c.getValue(q);
        }
        for(AdventureCard c : amours){
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

    public int hasTest(){
        for (Card c : hand) {
            if (c instanceof TestCard) {
                return 1;
            }
        }
        return 0;
    }

    public ArrayList<Card> getHand(){
        return hand;
    }

    public ArrayList<AllyCard> getAllies() {
        return allies;
    }

    public int getShields(){ return shields; }

    public void setShields(int shields) {
        this.shields = shields;
    }
}
