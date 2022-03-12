package com.team17.quest;

import java.util.ArrayList;

public class Stage {
    Card foe;
    ArrayList<Card> weapons;

    public Stage(){
        foe = null;
        weapons = new ArrayList<>();
    }

    public boolean playcard(Card c){
        if(c instanceof FoeCard && foe == null){
            foe = c;
            return true;
        }
        if(c instanceof WeaponCard){
            for(Card w : weapons){
                if(c.name == w.name){
                    return false;
                }
            }
            weapons.add(c);
            return true;
        }
        return false;
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
