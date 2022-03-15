package com.team17.quest;

public class WeaponCard extends AdventureCard{
    public WeaponCard(String name, int i, int bp){
        super(name, i, bp);
    }

    public int getValue(String q){
        return battle_points;
    }
}
