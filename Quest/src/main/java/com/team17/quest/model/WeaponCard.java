package com.team17.quest.model;

import com.team17.quest.model.AdventureCard;

public class WeaponCard extends AdventureCard {
    public WeaponCard(String name, int i, int bp){
        super(name, i, bp);
    }

    public int getValue(String q){
        return battle_points;
    }
}
