package com.team17.quest.model;

import com.team17.quest.model.AdventureCard;

import java.util.Arrays;
import java.util.List;

public class FoeCard extends AdventureCard {
    int bonus_points;
    String[] bonus_card;

    public FoeCard(String name, int i, int bp, int bonus, String[] bc){
        super(name, i, bp);
        bonus_points = bonus;
        bonus_card = bc;
    }

    public int getValue(String q){
        List<String> bonus_quests = Arrays.asList(bonus_card);
        if(bonus_quests.contains(q)){
            return bonus_points;
        }
        else{
            return battle_points;
        }
    }
}
