package com.team17.quest.model;

public abstract class AdventureCard extends Card {
    int battle_points;
    public AdventureCard(String name, int i, int bp){
        super(name, i);
        battle_points = bp;
    }

    public AdventureCard(){
        super();
        battle_points = 0;
    }

    public int getValue(String q){
        return battle_points;
    }
}