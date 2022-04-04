package com.team17.quest.model;

import java.util.Arrays;
import java.util.List;

public class AllyCard extends AdventureCard{
    int bids;
    int bonus_points;
    String bonus_quest;

    public AllyCard(String name, int i, int bp, int bonus, int b, String bq){
        super(name, i, bp);
        bids = b;
        bonus_points = bonus;
        bonus_quest = bq;
    }
    public int getValue(String q){
        if(bonus_quest.equals(q)){
            return bonus_points;
        }
        else{
            return battle_points;
        }
    }

    public int getBids() {
        return bids;
    }
}
