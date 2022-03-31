package com.team17.quest.model;

public class TournamentCard extends StoryCard{
    public int bonus_shields;
    public TournamentCard(String name, int i, int s){
        super(name, i);
        bonus_shields = s;
    }
}
