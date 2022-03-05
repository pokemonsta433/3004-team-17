package com.team17.quest;

public class FoeCard extends AdventureCard{
    int bonus_points;
    String[] bonus_card;

    public FoeCard(String name, int bp, int bonus, String[] bc){
        super(name, bp);
        bonus_points = bonus;
        bonus_card = bc;
    }

    public int getValue(Card c){
        if(c.name.equals(bonus_card)){
            return bonus_points;
        }
        else{
            return battle_points;
        }
    }
}
