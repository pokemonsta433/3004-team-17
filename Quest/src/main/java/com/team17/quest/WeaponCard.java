package com.team17.quest;

public class WeaponCard extends AdventureCard{
    public WeaponCard(String name, int bp){
        super(name, bp);
    }

    public int getValue(Card c){
        return battle_points;
    }
}
