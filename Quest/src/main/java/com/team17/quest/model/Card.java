package com.team17.quest.model;

import com.team17.quest.model.storyDeck.EventCard;
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

    public String classString(){
        //cannot make it a switch??
        if (this instanceof WeaponCard){
            return "weapon";
        } else if(this instanceof FoeCard){
            return "foe";
        } else if(this instanceof TestCard){
            return "test";
        } else if (this instanceof AllyCard){
            return "ally";
        } else return "amour";
    }
    public String getName(){return name;}

    public boolean isFoe(){
        return this instanceof FoeCard;
    }

    public boolean isWeapon(){
        return this instanceof WeaponCard;
    }

    public boolean isEvent() { return this instanceof EventCard; }
    public boolean isAlly(){
        return this instanceof AllyCard;
    }

    public int getId(){return id;}
}
