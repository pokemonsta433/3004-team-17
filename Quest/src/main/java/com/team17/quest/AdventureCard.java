package com.team17.quest;

public abstract class AdventureCard extends Card{
    int battle_points;
    public AdventureCard(String name, int bp){
        super(name);
        battle_points = bp;
    }

    public AdventureCard(){
        super();
        battle_points = 0;
    }
}
