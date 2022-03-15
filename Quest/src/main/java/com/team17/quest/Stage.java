package com.team17.quest;

import java.util.ArrayList;

public class Stage {
    //this class is irrelevant and will be reworked or removed in the future.
    Card foe;
    ArrayList<Card> weapons;

    public Stage(){
        foe = null;
        weapons = new ArrayList<>();
    }

    public void addCard(Card c){
        //this function is currently irrelevant and will be reworked in the future.
        if(c instanceof FoeCard) {
            foe = c;
        }
        if(c instanceof WeaponCard){
            weapons.add(c);

        }
    }

    public Card returnFoe(){
        Card f = foe;
        foe = null;
        return f;
    }

    public Card returnWeapon(int id){
        for(Card w : weapons){
            if(id == w.id){
                weapons.remove(w);
                return w;
            }
        }
        return null;
    }

    public ArrayList<Card> getWeapons(){
        return weapons;
    }

    public Card getFoe(){
        return foe;
    }
}
