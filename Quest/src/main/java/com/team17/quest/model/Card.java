package com.team17.quest.model;

import com.team17.quest.model.storyDeck.EventCardInterface;

public abstract class Card implements EventCardInterface {
    String name;
    int id;
    public Card(){
        name = "";
        id = 0;
    }
    public Card(String n, int i){
        name = n;
        id = i;
    }

    @Override
    public void eventStrategy(Game game) {

    }

    public String getName(){return name;}

    public boolean isFoe(){
        return this instanceof FoeCard;
    }

    public boolean isWeapon(){
        return this instanceof WeaponCard;
    }

    public boolean isAlly(){
        return false;
    }

    public int getId(){return id;}
}
